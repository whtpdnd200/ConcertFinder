package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertAndFavoritesLadderService {

    private final ConcertService concertService;
    private final FavoritesService favoritesService;

    // 즐겨찾기 저장시 콘서트 정보 같이 저장
    @Transactional
    public void insertFavoritesAndConcert(String concertId, long userId) {

        concertService.insertConcertInfo(concertId);
        favoritesService.addFavorites(concertId, userId);
    }
}
