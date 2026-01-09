package com.concertfinder.concertfinder.post.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostListDTO {

    private long id;

    private Character category;

    private String title;

    private long userId;

    private String userNickname;

    private LocalDateTime createdAt;
}
