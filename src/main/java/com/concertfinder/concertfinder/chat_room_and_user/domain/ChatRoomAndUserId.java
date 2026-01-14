package com.concertfinder.concertfinder.chat_room_and_user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomAndUserId {

    private long userId;

    private long roomId;
}
