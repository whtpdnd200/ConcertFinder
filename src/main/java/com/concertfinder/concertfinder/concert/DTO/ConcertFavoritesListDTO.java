package com.concertfinder.concertfinder.concert.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertFavoritesListDTO {

    private String concertId;

    private String concertName;

    private String posterPath;

    private String areaName;

}
