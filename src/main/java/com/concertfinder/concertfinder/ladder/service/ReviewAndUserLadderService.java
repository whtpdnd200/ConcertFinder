package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.review.DTO.ReviewListDTO;
import com.concertfinder.concertfinder.review.service.ReviewService;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewAndUserLadderService {

    private final ReviewService reviewService;

    public Page<ReviewListDTO> getReviewList(String areaCode, int page, int size, String orderType, Pageable pageable) {

        Page<ReviewListDTO> reviewList = reviewService.getFirstReviewList(areaCode, page, size, orderType, pageable);

        return reviewList;
    }
}
