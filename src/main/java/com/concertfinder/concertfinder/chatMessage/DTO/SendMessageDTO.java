package com.concertfinder.concertfinder.chatMessage.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendMessageDTO {

    private long roomId;

    private long userId;

    private String messageType;

    private String content;
}
