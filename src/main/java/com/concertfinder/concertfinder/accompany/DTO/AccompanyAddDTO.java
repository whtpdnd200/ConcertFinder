package com.concertfinder.concertfinder.accompany.DTO;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccompanyAddDTO {

    private Long postId;

    private Long userId;

    private Byte headCount;

    private String place;

    private LocalDateTime sDateTime;

}
