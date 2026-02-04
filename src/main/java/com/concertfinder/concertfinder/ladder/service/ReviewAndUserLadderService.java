package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.review.DTO.ReviewListDTO;
import com.concertfinder.concertfinder.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewAndUserLadderService {

    private final ReviewService reviewService;

    // 리뷰 목록 출력
    public Page<ReviewListDTO> getReviewList(String areaCode, int page, int size, String orderType, Pageable pageable) {

        Page<ReviewListDTO> reviewList = reviewService.getFirstReviewList(areaCode, page, size, orderType, pageable);

        return reviewList;
    }
}
