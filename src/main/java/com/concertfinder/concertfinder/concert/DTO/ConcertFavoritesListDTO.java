package com.concertfinder.concertfinder.concert.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConcertFavoritesListDTO {

    private String concertId;

    private String concertName;

    private String posterPath;

    private String areaName;

}
