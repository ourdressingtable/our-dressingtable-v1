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
public class PostLikeResponse {

    @Schema(description = "좋아요 여부", example = "true")
    private boolean liked;

}
