package com.concertfinder.concertfinder.accompany_count.domain;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccompanyCountId {

    private long accompanyId;

    private long userId;
}
