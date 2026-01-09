package com.concertfinder.concertfinder.comment.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentListDTO {

    private long id;

    private long postId;

    private long userId;

    private String userNickname;

    private String comment;

    private LocalDateTime createdAt;
}
