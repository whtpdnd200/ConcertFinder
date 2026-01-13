package com.concertfinder.concertfinder.accompany.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccompanyInfoDTO {

    private long id;

    private long userId;

    private byte headCount;

    private LocalDateTime sDateTime;

    private String place;

    private boolean isFull;

    private boolean isAccompanyChecked;

    private boolean isDateTimeAfter;

    private int currentCount;
}
