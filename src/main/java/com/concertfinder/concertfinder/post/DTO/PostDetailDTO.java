package com.concertfinder.concertfinder.post.DTO;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDetailDTO {

    private long id;

    private String concertId;

    private long userId;

    private String userNickname;

    private Character category;

    private String title;

    private String content;

    private int commentCount;

    // 동행 정보 객체
    private AccompanyInfoDTO accompanyInfoDTO;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
