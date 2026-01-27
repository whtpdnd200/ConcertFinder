package com.concertfinder.concertfinder.review.DTO;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewInfoDTO implements Serializable {

    private int reviewCount;

    private double reviewAveragePoint;
}
