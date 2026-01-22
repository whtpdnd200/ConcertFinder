package com.concertfinder.concertfinder.accompany_count;

import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accompany")
public class AccompanyCountRestController {

    private final AccompanyCountService accompanyCountService;

    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;

    // 동행 신청
    @PostMapping("/{accompanyId}")
    public ResponseEntity<ApiResponseDTO<Long>> addAccompany(@PathVariable long accompanyId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        accompanyAndAccompanyCountLadderService.insertAccompanyCountAndIsFullCheck(accompanyId, loginUserDTO.getId(), loginUserDTO.getNickname());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("동행 신청 성공"));
    }

    // 동행 신청 취소
    @DeleteMapping("/{accompanyId}")

    public ResponseEntity<ApiResponseDTO<Void>> removeAccompany(@PathVariable long accompanyId
                                                                , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        accompanyAndAccompanyCountLadderService.deleteAccompanyCountAndIsFullCheck(accompanyId, loginUserDTO.getId(), "exit");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("동행 취소 성공"));
    }


}
