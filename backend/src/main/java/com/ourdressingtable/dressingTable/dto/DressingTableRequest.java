package com.ourdressingtable.dressingTable.dto;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DressingTableRequest {

    @Schema(description = "화장대 이름", example = "my dt", required = true)
    @NotBlank
    private String name;

    @Schema(description = "화장대 이미지", example = "image_url")
    @URL
    private String imageUrl;

    @Builder
    public DressingTableRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public DressingTable toEntity(Member member) {
        return DressingTable.builder()
                .name(name)
                .imageUrl(imageUrl)
                .member(member)
                .build();

    }
}
