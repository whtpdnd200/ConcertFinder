package com.concertfinder.concertfinder.comment.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentListDTO {

    private long id;

    private long postId;

    private long userId;

    private String userNickname;

    private String comment;

    private LocalDateTime createdAt;
}
