package com.concertfinder.concertfinder.chat_room.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatUserInfoDTO {

    private long id;

    private String nickName;

    private boolean isHost;
}
