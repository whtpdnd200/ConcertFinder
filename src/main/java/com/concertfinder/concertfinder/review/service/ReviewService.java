package com.concertfinder.concertfinder.review.service;

import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.review.DTO.ReviewInfoDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewListDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewModifyDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewWriteDTO;
import com.concertfinder.concertfinder.review.domain.Review;
import com.concertfinder.concertfinder.review.repository.ReviewRepository;
import com.concertfinder.concertfinder.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String REVIEW_PREFIX = "area:review:";
    private static final String REVIEW_LIST_PREFIX = "area:review:list";

    // 리뷰 저장 메서드
    @Transactional
    public void insertReview(String areaCode
                            , long userId
                            , ReviewWriteDTO reviewWriteDTO) {

        String key = REVIEW_PREFIX + areaCode;
        String reviewInfoKey = REVIEW_LIST_PREFIX + areaCode;

        Review review = Review.builder()
                .areaCode(areaCode)
                .userId(userId)
                .point(reviewWriteDTO.getPoint())
                .review(reviewWriteDTO.getReview())
                .build();

        try {
            reviewRepository.save(review);

            redisTemplate.delete(key);
            redisTemplate.delete(reviewInfoKey);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 저장하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    public ReviewInfoDTO getReviewInfo(String areaCode) {

        String key = REVIEW_PREFIX + areaCode;

        Object cacheObject = redisTemplate.opsForValue().get(key);

        if(cacheObject != null) {

            ReviewInfoDTO cacheReview = objectMapper.convertValue(cacheObject, ReviewInfoDTO.class);

            log.info("redis Cache Hit! cacheReview : {} ", cacheReview);
            return cacheReview;
        }

        log.info("redis Cache Miss... areaCode : {} ", areaCode);

        ReviewInfoDTO reviewInfoDTO = ReviewInfoDTO.builder()
                .reviewCount(getReviewCounts(areaCode))
                .reviewAveragePoint(getAveragePoint(areaCode))
                .build();

        redisTemplate.opsForValue().set(key, reviewInfoDTO, java.time.Duration.ofHours(1));

        return reviewInfoDTO;
    }

    // 공연장의 리뷰 갯수 반환
    public int getReviewCounts(String areaCode) {

        return reviewRepository.countByAreaCode(areaCode);
    }

    // 공연장의 평균 별점 반환
    public Double getAveragePoint(String areaCode) {

        return reviewRepository.getAveragePointByAreaCode(areaCode);
    }

    // 리뷰 첫 페이지 목록 출력
    public Page<ReviewListDTO> getFirstReviewList(String areaCode, int page, int size, String orderType, Pageable pageable) {

        if(page == 0 && orderType.equals("desc")) {

            String key = REVIEW_LIST_PREFIX + areaCode;

            Object cacheReviews = redisTemplate.opsForValue().get(key);

            if(cacheReviews != null) {

                log.info("redis Cache Hit ReviewInfo");
                List<ReviewListDTO> reviewList = objectMapper.convertValue(cacheReviews, new TypeReference<List<ReviewListDTO>>() {});

                reviewList.forEach(dto -> dto.setUserNickname(userService.getNickname(dto.getUserId())));

                Page<ReviewListDTO> pageReviewList = new PageImpl<>(reviewList, pageable, reviewRepository.countByAreaCode(areaCode));

                return pageReviewList;
            }

            log.info("redis Cache Miss ReviewInfo");

            Page<ReviewListDTO> reviewPageDTO = getReviewList(areaCode, page, size, orderType, pageable);

            List<ReviewListDTO> reviews = new ArrayList<>(reviewPageDTO.getContent());

            //reviews.forEach(dto -> dto.setUserNickname(null));

            redisTemplate.opsForValue().set(key, reviews, java.time.Duration.ofMinutes(10));

            return reviewPageDTO;
        }

        log.info("redis Cache Miss Review Page Over");

        return getReviewList(areaCode, page, size, orderType, pageable);
    }

    // 리뷰 목록 출력
    public Page<ReviewListDTO> getReviewList(String areaCode, int page, int size, String orderType, Pageable pageable) {

        log.info("페이지 : {} ", page);

        log.info("정렬 기준 : {} ", orderType);

        Page<Review> reviews = null;

        if(orderType.equals("desc")) {
            reviews = reviewRepository.findAllByAreaCode(areaCode
                    , PageRequest.of(page, size
                            , Sort.by("createdAt").descending()));
        } else if(orderType.equals("highRating")) {
            reviews = reviewRepository.findAllByAreaCode(areaCode
                    , PageRequest.of(page, size
                            , Sort.by("point").descending()));
        } else {
            reviews = reviewRepository.findAllByAreaCode(areaCode
                    , PageRequest.of(page, size
                            , Sort.by("point").ascending()));
        }

        List<ReviewListDTO> reviewLists = new ArrayList<>();

        for(Review review : reviews) {

            ReviewListDTO reviewListDTO = ReviewListDTO.builder()
                    .id(review.getId())
                    .UserId(review.getUserId())
                    .userNickname(userService.getNickname(review.getUserId()))
                    .review(review.getReview())
                    .createdAt(review.getCreatedAt())
                    .point(review.getPoint())
                    .build();

            reviewLists.add(reviewListDTO);
        }

        Page<ReviewListDTO> reviewList = new PageImpl<>(reviewLists, pageable, reviewRepository.countByAreaCode(areaCode));

        return reviewList;
    }

    // 리뷰 내용 수정
    @Transactional
    public void updateReview(long reviewId, ReviewModifyDTO reviewModifyDTO, long userId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if(!optionalReview.isPresent()) {
            throw new NoSuchElementException("리뷰가 존재하지 않습니다!");
        }

        Review review = optionalReview.get();

        if(userId != review.getUserId()) {
            throw new UnAuthorizedException("다른 사용자의 리뷰는 수정 할 수 없습니다!");
        }

        String key = REVIEW_PREFIX + review.getAreaCode();
        String reviewInfoKey = REVIEW_LIST_PREFIX + review.getAreaCode();

        review = review.toBuilder()
                .review(reviewModifyDTO.getReview())
                .point(reviewModifyDTO.getPoint())
                .build();

        try {
            reviewRepository.save(review);

            redisTemplate.delete(key);
            redisTemplate.delete(reviewInfoKey);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 수정 하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    // 리뷰 삭제 메서드
    @Transactional
    public void deleteReview(long reviewId, long userId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if(!optionalReview.isPresent()) {
            throw new NoSuchElementException("리뷰가 존재하지 않습니다!");
        }

        Review review = optionalReview.get();

        if(userId != review.getUserId()) {
            throw new UnAuthorizedException("다른 사용자의 리뷰는 삭제 할 수 없습니다!");
        }

        String key = REVIEW_PREFIX + review.getAreaCode();
        String reviewInfoKey = REVIEW_LIST_PREFIX + review.getAreaCode();

        try {
            reviewRepository.delete(review);

            redisTemplate.delete(key);
            redisTemplate.delete(reviewInfoKey);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 삭제 하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    public void deleteUserReview() {

        List<Review> reviews = reviewRepository.findAll();

        if(!reviews.isEmpty()) {

            for(Review r : reviews) {

                if(!userService.isExistsUser(r.getUserId())) {

                    deleteReview(r.getId(), r.getUserId());
                }
            }
        }
    }

    public void deleteUserReview(List<Long> userIdList) {

        try {

            reviewRepository.deleteAllByUserIdIn(userIdList);
        } catch(DataAccessException e) {

            log.warn("탈퇴 회원 리뷰 목록 삭제 실패!");
        }
    }
}
