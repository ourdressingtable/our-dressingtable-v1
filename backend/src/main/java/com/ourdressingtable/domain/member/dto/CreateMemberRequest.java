package com.ourdressingtable.domain.member.dto;

import com.ourdressingtable.domain.member.domain.ColorType;
import com.ourdressingtable.domain.member.domain.Member;
import com.ourdressingtable.domain.member.domain.Role;
import com.ourdressingtable.domain.member.domain.SkinType;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateMemberRequest {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "별명을 입력해주세요")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNumber;

    private Role role;

    private SkinType skinType;

    private ColorType colorType;
    private Date birthDate;

    private String imageUrl;

    @Builder
    public CreateMemberRequest(String email, String password, String name, String nickname, String phoneNumber, Role role, SkinType skinType, ColorType colorType, Date birthDate, String imageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.skinType = skinType;
        this.colorType = colorType;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;

    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role(role)
                .skinType(skinType)
                .colorType(colorType)
                .birthDate(birthDate)
                .imageUrl(imageUrl)
                .build();

    }
}
