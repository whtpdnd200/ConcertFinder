package com.concertfinder.concertfinder.review;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.ReviewAndUserLadderService;
import com.concertfinder.concertfinder.review.DTO.ReviewListDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewModifyDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewWriteDTO;
import com.concertfinder.concertfinder.review.service.ReviewService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final ReviewAndUserLadderService reviewAndUserLadderService;

    // 리뷰 작성 기능
    @PostMapping("/{areaCode}")
    public ResponseEntity<ApiResponseDTO<Void>> writeReview(@PathVariable String areaCode
                                                           , @ModelAttribute @Valid ReviewWriteDTO reviewWriteDTO
                                                           , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        reviewService.insertReview(areaCode, loginUserDTO.getId(), reviewWriteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("리뷰 작성 성공"));
    }

    // 리뷰 목록 출력 기능
    @GetMapping("/{areaCode}")
    public ResponseEntity<ApiResponseDTO<Page<ReviewListDTO>>> getReviewList(@PathVariable String areaCode
                                                                            , @RequestParam int size
                                                                            , @RequestParam int page
                                                                            , @RequestParam String orderType
                                                                            , Pageable pageable) {

        Page<ReviewListDTO> reviewList = reviewAndUserLadderService.getReviewList(areaCode, page, size, orderType, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("리뷰 목록 출력 성공", reviewList));
    }

    // 리뷰 수정 기능
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponseDTO<Void>> modifyReview(@PathVariable @NotNull(message = "수정 할 리뷰가 존재 하지 않습니다!") Long reviewId
                                                            , @RequestBody @Valid ReviewModifyDTO reviewModifyDTO
                                                            , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        reviewService.updateReview(reviewId, reviewModifyDTO, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("리뷰 수정 완료"));
    }

    // 리뷰 삭제
    @DeleteMapping("{reviewId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeReview(@PathVariable @NotNull(message = "삭제 할 리뷰가 존재 하지 않습니다!") Long reviewId
                                                            , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        reviewService.deleteReview(reviewId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("리뷰 삭제 완료"));
    }
}
