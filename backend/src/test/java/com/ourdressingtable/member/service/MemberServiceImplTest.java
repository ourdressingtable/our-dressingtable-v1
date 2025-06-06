package com.ourdressingtable.member.service;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.impl.MemberServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WithdrawalMemberService withdrawalMemberService;

    @Nested
    @DisplayName("회원 가입 테스트")
    class singUp {
        @DisplayName("회원 가입 성공")
        @Test
        public void createMember_shouldReturnSuccess() {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();

            String encodedPassword = "password";
            Member member = createMemberRequest.toEntity(encodedPassword);
            ReflectionTestUtils.setField(member,"id",1L);

            when(passwordEncoder.encode(createMemberRequest.getPassword())).thenReturn(encodedPassword);
            when(memberRepository.save(any(Member.class))).thenReturn(member);

            // when
            Long savedId = memberService.createMember(createMemberRequest);

            //then
            assertNotNull(savedId);
            assertEquals(createMemberRequest.getEmail(), member.getEmail());

        }

        @DisplayName("회원 가입 실패_중복 이메일")
        @Test
        public void createMember_withDuplicateEmail_shouldReturnError() {
            // given
            CreateMemberRequest createMemberRequest = TestDataFactory.testCreateMemberRequest();

            when(memberRepository.existsByEmail(createMemberRequest.getEmail())).thenReturn(true);

            // when & then
            OurDressingTableException ourDressingTableException = assertThrows(OurDressingTableException.class, () -> memberService.createMember(createMemberRequest));
            assertEquals(ourDressingTableException.getHttpStatus(), ErrorCode.EMAIL_ALREADY_EXISTS.getHttpStatus());
            assertEquals(ourDressingTableException.getMessage(), ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());

        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class findUser {

        @DisplayName("회원 조회 성공 테스트")
        @Test
        public void findMember_ShouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);

            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            // when
            OtherMemberResponse findMember = memberService.getOtherMember(member.getId());

            //then
            assertEquals(findMember.getNickname(),member.getNickname());

        }

        @DisplayName("회원 조회 실패 테스트 - USER NOT FOUND")
        @Test
        public void findMember_ShouldReturnUserNotFoundError() {
            // given
            Member member = TestDataFactory.testMember(1L);
            given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

            // when
            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> memberService.getOtherMember(member.getId()));

            //then
            assertEquals(exception.getHttpStatus(), ErrorCode.MEMBER_NOT_FOUND.getHttpStatus());

        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class updateUser {

        @DisplayName("회원 정보 수정 성공 테스트")
        @Test
        public void updateMember_ShouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            try(MockedStatic<SecurityUtil> mockedStatic = Mockito.mockStatic(SecurityUtil.class)) {
                mockedStatic.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

                // when
                memberService.updateMember(updateMemberRequest);

                //then
                assertEquals(member.getNickname(), "new me");
            }

        }

        @DisplayName("회원 정보 수정 실패 테스트 - 탈퇴한 회원")
        @Test
        public void updateMember_ShouldReturnUserNotFoundError() {
            // given
            Member member = TestDataFactory.testMember(1L);
            member.withdraw();
            UpdateMemberRequest updateMemberRequest = TestDataFactory.testUpdateMemberRequest();

            try(MockedStatic<SecurityUtil> mockedStatic = Mockito.mockStatic(SecurityUtil.class)) {
                mockedStatic.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

                // when & then
                OurDressingTableException exception = assertThrows(OurDressingTableException.class,
                        () -> memberService.updateMember(updateMemberRequest));
                assertEquals(ErrorCode.MEMBER_NOT_ACTIVE.getCode(), exception.getCode());
            }
        }
    }

    @Nested
    @DisplayName("회원 삭제 테스트")
    class deleteUser {

        @DisplayName("회원 삭제 성공")
        @Test
        public void deleteMember_ShouldReturnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();

            try (MockedStatic<SecurityUtil> mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class)) {
                mockedSecurityUtil.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
                when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

                // when
                memberService.withdrawMember(withdrawalMemberRequest);

                // then
                assertEquals(Status.WITHDRAWAL, member.getStatus());
                verify(withdrawalMemberService).createWithdrawalMember(withdrawalMemberRequest, member);
            }

        }

        @DisplayName("회원 삭제 실패 - 이미 탈퇴한 회원")
        @Test
        public void updateMember_ShouldReturnUserNotFoundError() {
            // given
            Long memberId = 2L;
            Member member = TestDataFactory.testMember(2L);
            member.withdraw();

            WithdrawalMemberRequest withdrawalMemberRequest = TestDataFactory.testWithdrawalMemberRequest();
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            assertThrows(OurDressingTableException.class, () -> {
                memberService.withdrawMember(withdrawalMemberRequest);
            });


        }
    }
}
