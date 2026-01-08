package com.concertfinder.concertfinder.favorites.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoritesId {

    private long userId;

    private String concertId;
}
