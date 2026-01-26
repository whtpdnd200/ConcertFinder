package com.concertfinder.concertfinder.favorites.domain;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoritesId {

    private long userId;

    private String concertId;
}
