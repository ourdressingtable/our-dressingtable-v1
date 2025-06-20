package com.ourdressingtable.security.controller;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.security.auth.JwtTokenProvider;
import com.ourdressingtable.security.auth.RedisTokenService;
import com.ourdressingtable.security.auth.email.service.EmailVerificationService;
import com.ourdressingtable.security.auth.email.dto.ConfirmEmailVerificationCodeRequest;
import com.ourdressingtable.security.auth.email.dto.SendEmailVerificationCodeRequest;
import com.ourdressingtable.security.dto.LoginRequest;
import com.ourdressingtable.security.dto.LoginResponse;
import com.ourdressingtable.security.dto.RefreshTokenRequest;
import com.ourdressingtable.security.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisTokenService redisTokenService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Member member = memberService.getActiveMemberEntityByEmail(request.getEmail());

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getEmail(), member.getRole());

        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getEmail(), member.getRole());

        redisTokenService.saveTokenInfo(member.getEmail(), accessToken, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"),"accessToken");
        redisTokenService.saveTokenInfo(member.getEmail(), refreshToken, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"),"refreshToken");
        return ResponseEntity.ok(LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).memberId(member.getId()).email(member.getEmail()).name(member.getName()).nickname(member.getNickname()).imageUrl(member.getImageUrl()).build());
    }

    @PostMapping("/refresh")
    @Operation(summary = "token 재발급", description = "refresh token과 access token을 재발급합니다.")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) {
        String token = request.getRefreshToken();

        if(!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getEmail(token);
        boolean isValid = redisTokenService.validate(email, token, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), "refreshToken");

        if(!isValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Member member = memberService.getActiveMemberEntityByEmail(email);

        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email, member.getRole());

        redisTokenService.saveTokenInfo(email, newRefreshToken, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent"), "refreshToken");

        return ResponseEntity.ok(TokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.")
    public ResponseEntity logout(HttpServletRequest httpServletRequest) {
        log.info("[logout] 호출");
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        log.info("[logout] token = {} " + token + "");
        if(token != null && jwtTokenProvider.validateToken(token)) {
            long remaining = jwtTokenProvider.getExpirationTime(token);
            redisTokenService.blacklistAccessToken(token, remaining);

            String email = jwtTokenProvider.getEmail(token);
            redisTokenService.deleteTokenInfo(email, "refreshToken", httpServletRequest.getHeader("User-Agent"));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verification-code/email")
    @Operation(summary = "이메일 인증코드 전송", description = "이메일로 인증코드를 전송합니다.")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody @Valid SendEmailVerificationCodeRequest request) {
        emailVerificationService.sendVerificationEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-verification-code/email")
    @Operation(summary = "이메일 인증코드 확인", description = "입력한 인증코드가 유효한지 확인합니다.")
    public ResponseEntity<Void> confirmVerificationCode(@RequestBody @Valid ConfirmEmailVerificationCodeRequest request) {
        emailVerificationService.confirmVerification(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok().build();
    }



}
