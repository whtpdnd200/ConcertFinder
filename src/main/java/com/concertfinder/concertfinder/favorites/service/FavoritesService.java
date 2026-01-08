package com.concertfinder.concertfinder.favorites.service;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.exception.GlobalExceptionHandler;
import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.repository.FavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoritesService{

    private final FavoritesRepository favoritesRepository;
    private final ConcertService concertService;

    // 콘서트 정보 즐겨찾기
    @Transactional
    public void addFavorites(String concertId, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        concertService.insertConcertInfo(concertId);

        Favorites favorites = Favorites.builder()
                .userId(userId)
                .concertId(concertId)
                .build();

        try {
            favoritesRepository.save(favorites);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 오류로 즐겨찾기 정보를 저장하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    public boolean isFavorites(String concertId, long userId) {

        return favoritesRepository.existsByUserIdAndConcertId(userId, concertId);
    }
}
