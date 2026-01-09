package com.concertfinder.concertfinder.review.service;

import com.concertfinder.concertfinder.review.DTO.ReviewWriteDTO;
import com.concertfinder.concertfinder.review.domain.Review;
import com.concertfinder.concertfinder.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
    public double getAveragePoint(String areaCode) {

        return reviewRepository.getAveragePointByAreaCode(areaCode);
    }
}
