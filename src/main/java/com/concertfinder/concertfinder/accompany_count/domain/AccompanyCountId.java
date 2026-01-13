package com.concertfinder.concertfinder.accompany_count.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccompanyCountId {

    private long accompanyId;

    private long userId;
}
