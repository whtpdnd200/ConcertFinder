package com.concertfinder.concertfinder.review.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewWriteDTO {

    @NotBlank(message = "리뷰는 비어 있을 수 없습니다!")
    private String review;

    private double point;
}
