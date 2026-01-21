package com.concertfinder.concertfinder.last_chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LastChatId {

    private long roomId;

    private long userId;
}
