package com.concertfinder.concertfinder.post.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListDTO {

    private long id;

    private Character category;

    private String title;

    private long userId;

    private String userNickname;

    private LocalDateTime createdAt;
}
