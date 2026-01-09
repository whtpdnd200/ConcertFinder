package com.concertfinder.concertfinder.review.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModifyDTO {

    private String review;

    private double point;
}
