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

    private byte headCount;

    private LocalDateTime sDateTime;

    private String place;

    private int currentCount;
}
