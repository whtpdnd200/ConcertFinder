package com.concertfinder.concertfinder.chat_room.DTO;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomInfoDTO {

    private long chatRoomId;

    private List<ChatUserInfoDTO> userInfoList;
}
