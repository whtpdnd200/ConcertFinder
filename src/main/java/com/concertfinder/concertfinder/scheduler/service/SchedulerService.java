package com.concertfinder.concertfinder.scheduler.service;

import com.concertfinder.concertfinder.concert.domain.Concert;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerService {

    private final ConcertService concertService;
    private final FavoritesService favoritesService;


    @Transactional
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void concertDeleteByCron() {

        List<Concert> concerts = concertService.getConcertList();

        for(Concert concert : concerts) {

            if(!favoritesService.isFavorites(concert.getConcertId())
                    && !concert.getState().equals("공연중")
                    && !concert.getState().equals("공연예정")) {

                concertService.deleteConcert(concert);
            }
        }
    }
}
