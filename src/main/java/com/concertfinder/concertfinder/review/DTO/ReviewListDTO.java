package com.concertfinder.concertfinder.review.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDTO {

    private long id;

    private long UserId;

    private String userNickname;

    private double point;

    private String review;

    private LocalDateTime createdAt;

}
