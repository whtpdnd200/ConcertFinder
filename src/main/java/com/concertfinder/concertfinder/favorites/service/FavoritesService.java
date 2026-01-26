package com.concertfinder.concertfinder.favorites.service;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.exception.GlobalExceptionHandler;
import com.concertfinder.concertfinder.favorites.DTO.FavoritesConcertIdDTO;
import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.repository.FavoritesRepository;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoritesService{

    private final FavoritesRepository favoritesRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String FAVS_PREFIX = "user:favs:";


    // 콘서트 정보 즐겨찾기
    @Transactional
    public void addFavorites(String concertId, Long userId) {

        String key = FAVS_PREFIX + userId;

        GlobalExceptionHandler.loginException(userId);

        Favorites favorites = Favorites.builder()
                .userId(userId)
                .concertId(concertId)
                .build();

        try {
            favoritesRepository.save(favorites);

            // NPE 방지
            if(Boolean.TRUE.equals(redisTemplate.hasKey(key))) {

                redisTemplate.opsForSet().add(key, concertId);
                redisTemplate.expire(key, java.time.Duration.ofHours(1));
            }
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 오류로 즐겨찾기 정보를 저장하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    // 즐겨찾기 정보 삭제
    @Transactional
    public void deleteFavorites(String concertId, long userId) {

        String key = FAVS_PREFIX + userId;

        Optional<Favorites> optionalFavorites = favoritesRepository.findByUserIdAndConcertId(userId, concertId);

        if(!optionalFavorites.isPresent()) {

            throw new NoSuchElementException("즐겨찾기 정보를 찾을 수 없습니다!");
        }

        try {
            favoritesRepository.delete(optionalFavorites.get());
            redisTemplate.delete(key);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 오류로 즐겨찾기 삭제에 실패 했습니다 잠시 후 다시 시도 해주세요!");
        }
    }

    // 유저가 해당 콘서트를 즐겨찾기 했는지 확인하는 메서드
    public boolean isFavorites(String concertId, long userId) {

        return favoritesRepository.existsByUserIdAndConcertId(userId, concertId);
    }

    public boolean isFavorites(String concertId) {
        return favoritesRepository.existsByConcertId(concertId);
    }

    // 유저의 즐겨찾기 정보 캐싱
    public Set<Object> addCacheFavorites(long userId) {

        String key = FAVS_PREFIX + userId;

        Set<Object> favs = redisTemplate.opsForSet().members(key);

        if(favs == null || favs.isEmpty()) {

            List<String> favsList = getFavoritesConcertIds(userId);

            if(!favsList.isEmpty()) {

                redisTemplate.opsForSet().add(key, favsList.toArray());
                redisTemplate.expire(key, java.time.Duration.ofHours(1));
                return new HashSet<>(favsList);
            }
        }

        return favs;
    }

    public List<String> getFavoritesConcertIds(long userId) {

        List<Favorites> favoritesList = favoritesRepository.findAllByUserId(userId);

        List<String> favoritesConcertIdList = new ArrayList<>();

        for(Favorites favorites : favoritesList) {

            favoritesConcertIdList.add(favorites.getConcertId());
        }
        return favoritesConcertIdList;
    }

    public void deleteUserFavorites() {

        List<Favorites> favorites = favoritesRepository.findAll();

        if(!favorites.isEmpty()) {

            for(Favorites f : favorites) {

                if(!userService.isExistsUser(f.getUserId())) {

                    deleteFavorites(f.getConcertId(), f.getUserId());
                }
            }
        }
    }

    public void deleteUserFavorites(List<Long> userIdList) {

        try {

            favoritesRepository.deleteAllByUserIdIn(userIdList);
        } catch(DataAccessException e) {

            log.warn("탈퇴 회원 즐겨찾기 삭제 실패");
        }

    }
}
