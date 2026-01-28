package com.concertfinder.concertfinder.kafka.DTO;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeDTO {

    private Long receiverId;
    private String message;
    private String noticeType;
    private String url;
}
