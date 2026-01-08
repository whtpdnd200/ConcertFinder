package com.concertfinder.concertfinder.post.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostDetailDTO {

    private long id;

    private String concertId;

    private long userId;

    private String userNickname;

    private Character category;

    private String title;

    private String content;

    private int commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
