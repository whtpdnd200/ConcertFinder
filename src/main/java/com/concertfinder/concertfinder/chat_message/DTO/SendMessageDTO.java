package com.concertfinder.concertfinder.chat_message.DTO;

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
