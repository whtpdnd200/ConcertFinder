package com.concertfinder.concertfinder.review.service;

import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.review.DTO.ReviewListDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewModifyDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewWriteDTO;
import com.concertfinder.concertfinder.review.domain.Review;
import com.concertfinder.concertfinder.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 저장 메서드
    public void insertReview(String areaCode
                            , long userId
                            , ReviewWriteDTO reviewWriteDTO) {

        if(reviewWriteDTO.getReview().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰는 비어있을 수 없습니다!");
        }

        Review review = Review.builder()
                .areaCode(areaCode)
                .userId(userId)
                .point(reviewWriteDTO.getPoint())
                .review(reviewWriteDTO.getReview())
                .build();

        try {
            reviewRepository.save(review);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 저장하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    // 공연장의 리뷰 갯수 반환
    public int getReviewCounts(String areaCode) {

        return reviewRepository.countByAreaCode(areaCode);
    }

    // 공연장의 평균 별점 반환
    public Double getAveragePoint(String areaCode) {

        return reviewRepository.getAveragePointByAreaCode(areaCode);
    }

    // 리뷰 목록 출력
    public Page<ReviewListDTO> getReviewList(String areaCode, int page, int size, String orderType, Pageable pageable) {

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
    public void updateReview(long reviewId, ReviewModifyDTO reviewModifyDTO, long userId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if(!optionalReview.isPresent()) {
            throw new NoSuchElementException("댓글이 존재하지 않습니다!");
        }

        Review review = optionalReview.get();

        if(userId != review.getUserId()) {
            throw new UnAuthorizedException("다른 사용자의 리뷰는 수정 할 수 없습니다!");
        }

        review = review.toBuilder()
                .review(reviewModifyDTO.getReview())
                .point(reviewModifyDTO.getPoint())
                .build();

        try {
            reviewRepository.save(review);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 수정 하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }

    // 리뷰 삭제 메서드
    public void deleteReview(long reviewId, long userId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if(!optionalReview.isPresent()) {
            throw new NoSuchElementException("댓글이 존재하지 않습니다!");
        }

        Review review = optionalReview.get();

        if(userId != review.getUserId()) {
            throw new UnAuthorizedException("다른 사용자의 리뷰는 삭제 할 수 없습니다!");
        }


        try {
            reviewRepository.delete(review);

        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 리뷰 정보를 삭제 하지 못했습니다! 잠시 후 다시 시도해주세요!");
        }
    }
}
