package com.concertfinder.concertfinder.chat_room.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomListDTO {

    private long roomId;

    private String roomName;

    private int headCount;

    private int currentCount;

    private boolean isPrivate;

    private boolean isFull;

    private boolean isDateAfter;
}
