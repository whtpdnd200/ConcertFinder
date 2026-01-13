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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accompany")
public class AccompanyRestController {

    private final AccompanyCountService accompanyCountService;

    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;

    @PostMapping("/{accompanyId}")
    public ResponseEntity<ApiResponseDTO<Void>> addAccompany(@PathVariable long accompanyId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        accompanyAndAccompanyCountLadderService.insertAccompanyCountAndIsFullCheck(accompanyId, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("동행 신청 성공"));
    }
}
