package com.concertfinder.concertfinder.chat_room_and_user.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatUserInfoDTO {

    private long id;

    private long roomId;

    private String nickname;

    private boolean isHost;
}
