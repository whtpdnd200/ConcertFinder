package com.concertfinder.concertfinder.concert.service;

import com.concertfinder.concertfinder.concert.DTO.ConcertFavoritesListDTO;
import com.concertfinder.concertfinder.concert.DTO.addConcertInfoDTO.AddConcertInfoDTO;
import com.concertfinder.concertfinder.concert.DTO.addConcertInfoDTO.ResponsesAddDTO;
import com.concertfinder.concertfinder.concert.DTO.areaDTO.ResponsesAreaDTO;
import com.concertfinder.concertfinder.concert.DTO.concertListDTO.ConcertInfoDTO;
import com.concertfinder.concertfinder.concert.DTO.concertListDTO.ResponsesListDTO;
import com.concertfinder.concertfinder.concert.DTO.infoDTO.ResponsesInfoDTO;
import com.concertfinder.concertfinder.concert.domain.Concert;
import com.concertfinder.concertfinder.concert.repository.ConcertRepository;
import com.concertfinder.concertfinder.config.properties.KopisProperties;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.review.DTO.ReviewInfoDTO;
import com.concertfinder.concertfinder.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final WebClient kopisWebClient;
    private final KopisProperties kopisProperties;
    private final ConcertRepository concertRepository;
    private final FavoritesService favoritesService;
    private final ReviewService reviewService;


    // 즐겨찾기 했는지 안했는지를 판별하는 isFavorites 값 추가 메서드
    public ResponsesListDTO addIsFavorites(ResponsesListDTO responsesDTO, long userId) {

        Set<Object> favList = favoritesService.addCacheFavorites(userId);

        if(responsesDTO == null || responsesDTO.getLists() == null) {
            throw new NoSuchElementException("콘서트 목록을 불러 올 수 없습니다! 잠시 후 다시 시도 해주세요!");
        }

        for(ConcertInfoDTO concertInfoDTO : responsesDTO.getLists()) {

            concertInfoDTO.setFavorites(favList.contains(concertInfoDTO.getConcertId()));
        }

        return responsesDTO;
    }

    // 다음 페이지가 있는지를 판단하는 hasNext값을 추가 해주는 메서드
    public ResponsesListDTO addHasNext(ResponsesListDTO responsesDTO, int rows) {

        if(responsesDTO != null && responsesDTO.getLists() != null) {
            int size = responsesDTO.getLists().size();
            if(size > rows) {
                responsesDTO.setHasNext(true);
                responsesDTO.getLists().remove(size - 1);
            } else {
                responsesDTO.setHasNext(false);
            }
        }

        return responsesDTO;
    }

    // 기본 화면의 콘서트 목록 출력 메서드
    public ResponsesListDTO getList(String code
                                    , Integer page
                                    , String areaCode
                                    , String keyword
                                    , long userId) {

        if(page == null) {
            page = 1;
        }

        if(areaCode != null) {
            code = areaCode;
        }

        if(areaCode != null && areaCode.equals("00")) {
            code = "";
        }

        final String fCode = code;

        final int cPage = page;

        final int rows = 8;

        ResponsesListDTO responsesDTO = kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr")
                        .queryParam("service", kopisProperties.getKey())
                        .queryParam("stdate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("eddate", LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .queryParam("cpage", cPage)
                        .queryParam("rows", rows + 1)
                        .queryParam("shcate", "CCCD")
                        .queryParam("signgucode", fCode)
                        .queryParam("shprfnm", keyword)
                        .build())
                .retrieve()
                .bodyToMono(ResponsesListDTO.class)
                .block();

        responsesDTO = addIsFavorites(responsesDTO, userId);

        return addHasNext(responsesDTO, rows);
    }

    // 특정 단일 항목의 콘서트 API를 호출해 DTO에 저장하는 메서드
    public ResponsesInfoDTO getConcertInfo(String concertId, long userId) {

        ResponsesInfoDTO responsesInfoDTO = kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr/{concertId}")
                        .queryParam("service", kopisProperties.getKey())
                        .build(concertId))
                .retrieve()
                .bodyToMono(ResponsesInfoDTO.class)
                .block();

        String areaId = responsesInfoDTO.getInfoDTO().getAreaCode();

        ReviewInfoDTO reviewInfoDTO = reviewService.getReviewInfo(areaId);

        responsesInfoDTO.getInfoDTO().setAverageReview(reviewInfoDTO.getReviewAveragePoint());
        responsesInfoDTO.getInfoDTO().setReviewCount(reviewInfoDTO.getReviewCount());
        responsesInfoDTO.getInfoDTO().setFavorites(favoritesService.isFavorites(concertId, userId));
        responsesInfoDTO.getInfoDTO().setAreaInfo(getAreaInfo(areaId).getAreaInfoDTO());
        return responsesInfoDTO;
    }

    // 콘서트 상세 DTO에 들어갈 경도 위도 주소를 저장하기 위해 호출하는 API 메서드
    public ResponsesAreaDTO getAreaInfo(String areaCode) {

        return kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/prfplc/{areaId}")
                        .queryParam("service", kopisProperties.getKey())
                        .build(areaCode))
                .retrieve()
                .bodyToMono(ResponsesAreaDTO.class)
                .block();
    }

    // db에 콘서트 정보 저장 할 dto 생성 메서드
    public AddConcertInfoDTO getAddConcertInfo(String concertId) {

        ResponsesAddDTO responsesAddDTO = kopisWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pblprfr/{concertId}")
                        .queryParam("service", kopisProperties.getKey())
                        .build(concertId))
                .retrieve()
                .bodyToMono(ResponsesAddDTO.class)
                .block();

        if(responsesAddDTO == null || responsesAddDTO.getAddConcertInfoDTO() == null) {

            throw new NoSuchElementException("콘서트 정보가 없습니다! 잠시 후 다시 시도 해주세요!");
        }

        return responsesAddDTO.getAddConcertInfoDTO();
    }

    // db에 콘서트 정보 저장
    public void insertConcertInfo(String concertId) {

        if(concertRepository.existsByConcertId(concertId)) {
            return;
        }

        AddConcertInfoDTO addConcertInfoDTO = getAddConcertInfo(concertId);

        Concert concert = Concert.builder()
                .concertId(addConcertInfoDTO.getConcertId())
                .concertName(addConcertInfoDTO.getConcertName())
                .posterPath(addConcertInfoDTO.getPosterPath())
                .areaCode(addConcertInfoDTO.getAreaCode())
                .areaName(addConcertInfoDTO.getAreaName())
                .state(addConcertInfoDTO.getState())
                .build();

        try {
            concertRepository.save(concert);
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 오류로 공연 정보를 저장하지 못했습니다 잠시 후 다시 시도 해주세요!");
        }
    }

    // 유저의 콘서트 즐겨찾기 리스트 출력 메서드
    public List<ConcertFavoritesListDTO> getConcertList(long userId) {

        List<String> favoritesConcertIdList = favoritesService.getFavoritesConcertIds(userId);

        List<Concert> concerts = concertRepository.findAllByConcertId(favoritesConcertIdList);

        List<ConcertFavoritesListDTO> concertList = new ArrayList<>();

        for(Concert concert : concerts) {

            ConcertFavoritesListDTO concertFavoritesListDTO = ConcertFavoritesListDTO.builder()
                    .concertId(concert.getConcertId())
                    .concertName(concert.getConcertName())
                    .posterPath(concert.getPosterPath())
                    .areaName(concert.getAreaName())
                    .build();

            concertList.add(concertFavoritesListDTO);
        }
        return concertList;
    }

    // 유저의 즐겨찾기 콘서트 리스트 3개 출력 메서드
    public List<ConcertFavoritesListDTO> getConcertListTop3(long userId) {

        List<String> favoritesConcertIdList = favoritesService.getFavoritesConcertIds(userId);

        if(favoritesConcertIdList.isEmpty() || favoritesConcertIdList == null) {
            return new ArrayList<>();
        }

        List<Concert> concerts = concertRepository.findAllTop3ByConcertId(favoritesConcertIdList, PageRequest.of(0, 3));

        List<ConcertFavoritesListDTO> concertList = new ArrayList<>();

        for(Concert concert : concerts) {

            ConcertFavoritesListDTO concertFavoritesListDTO = ConcertFavoritesListDTO.builder()
                    .concertId(concert.getConcertId())
                    .concertName(concert.getConcertName())
                    .posterPath(concert.getPosterPath())
                    .areaName(concert.getAreaName())
                    .build();

            concertList.add(concertFavoritesListDTO);
        }
        return concertList;
    }

    // 콘서트 아이디 리스트 반환
    public List<Concert> getConcertList() {

        return concertRepository.findAll();

    }

    public void deleteConcert(Concert concert) {

        if(concert == null) {
            throw new NoSuchElementException("정보 없음");
        }

        try {
            concertRepository.delete(concert);
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러");
        }
    }
}
