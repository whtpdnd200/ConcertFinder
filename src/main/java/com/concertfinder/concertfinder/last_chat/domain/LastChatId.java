package com.concertfinder.concertfinder.last_chat.domain;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LastChatId {

    private long roomId;

    private long userId;
}
