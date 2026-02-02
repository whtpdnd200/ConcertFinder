package com.concertfinder.concertfinder.notice.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSendDTO {

    private long id;
    private Long receiverId;
    private String message;
    private String noticeType;
    private String url;
    private LocalDateTime createdAt;
}
