package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @Schema(description = "게시글 제목", example = "이 화장품 어때요?")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "게시글 내용", example = "사용해보신 분 후기 알려주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Schema(description = "카테고리", example = "1")
    @NotNull(message = "카테고리를 선택해주세요.")
    private Long communityCategoryId;

    @Schema(description = "이미지", example = "{https://image1.img, {https://image2.img}")
    private List<String> images;

    @Builder
    public CreatePostRequest(String title, String content, Long communityCategoryId, List<String> images) {
        this.title = title;
        this.content = content;
        this.communityCategoryId = communityCategoryId;
        this.images = images;

    }
}

