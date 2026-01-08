package com.concertfinder.concertfinder.common.service;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicService {

    private final ConcertService concertService;
    private final FavoritesService favoritesService;


    @Transactional
    public void insertFavoritesAndConcert(String concertId, long userId) {

        concertService.insertConcertInfo(concertId);
        favoritesService.addFavorites(concertId, userId);
    }
}
