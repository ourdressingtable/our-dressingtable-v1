package com.ourdressingtable.community.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostResponse {

    @Schema(description = "생성된 게시글 ID", example = "1")
    private Long id;

}
