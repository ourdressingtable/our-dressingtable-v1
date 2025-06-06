package com.ourdressingtable.communityCategory.dto;


import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCategoryResponse {
    private Long id;
    private String name;

    @Builder
    public CommunityCategoryResponse(String name) {
        this.name = name;
    }

    public static CommunityCategoryResponse from(CommunityCategory category) {
        return CommunityCategoryResponse.builder()
                .name(category.getName())
                .build();
    }
}
