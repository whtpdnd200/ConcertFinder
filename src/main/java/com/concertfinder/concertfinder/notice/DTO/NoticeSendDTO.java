package com.concertfinder.concertfinder.notice.DTO;

import lombok.*;

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
}
