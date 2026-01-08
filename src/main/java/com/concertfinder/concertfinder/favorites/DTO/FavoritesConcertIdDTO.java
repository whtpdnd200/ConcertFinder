package com.concertfinder.concertfinder.favorites.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoritesConcertIdDTO {

    private String concertId;
}
