package com.concertfinder.concertfinder.review;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.review.DTO.ReviewWriteDTO;
import com.concertfinder.concertfinder.review.service.ReviewService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewRestController {

    private final ReviewService reviewService;

    @PostMapping("/{areaCode}")
    public ResponseEntity<ApiResponseDTO<Void>> writeReview(@PathVariable String areaCode
                                                           , @ModelAttribute ReviewWriteDTO reviewWriteDTO
                                                           , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        reviewService.insertReview(areaCode, loginUserDTO.getId(), reviewWriteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("리뷰 작성 성공"));
    }
}
