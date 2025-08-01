package com.ourdressingtable.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "채팅방 생성 응답 DTO")
public record ChatroomIdResponse (

    @Schema(description = "생성된 채팅방 ID", example = "1")
    String chatroomId

){}
