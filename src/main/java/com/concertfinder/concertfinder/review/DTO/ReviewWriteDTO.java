package com.concertfinder.concertfinder.review.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewWriteDTO {

    private String review;

    private double point;
}
