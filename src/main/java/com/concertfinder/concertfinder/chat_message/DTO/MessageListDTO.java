package com.concertfinder.concertfinder.chat_message.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageListDTO {

    private long id;

    private String type;

    private String userNickname;

    private String content;

    private boolean reverse;

    private LocalDateTime createdAt;
}
