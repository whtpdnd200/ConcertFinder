package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.concert.DTO.ConcertFavoritesListDTO;
import com.concertfinder.concertfinder.concert.DTO.areaDTO.ResponsesAreaDTO;
import com.concertfinder.concertfinder.concert.DTO.infoDTO.ResponsesInfoDTO;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertRestController {

    private final ConcertService concertService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<ApiResponseDTO<List<ConcertFavoritesListDTO>>> getFavoritesConcertLists(@PathVariable long userId) {

        List<ConcertFavoritesListDTO> concertFavoritesList = concertService.getConcertList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("목록 출력 성공", concertFavoritesList));
    }
}
