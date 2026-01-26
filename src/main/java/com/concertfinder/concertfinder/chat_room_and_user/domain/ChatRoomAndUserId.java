package com.concertfinder.concertfinder.chat_room_and_user.domain;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatRoomAndUserId {

    private long userId;

    private long roomId;
}
