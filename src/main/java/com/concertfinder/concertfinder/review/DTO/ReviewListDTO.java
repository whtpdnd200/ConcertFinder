package com.concertfinder.concertfinder.review.DTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDTO implements Serializable {

    private long id;

    private long UserId;

    private String userNickname;

    private double point;

    private String review;

    private LocalDateTime createdAt;

}
