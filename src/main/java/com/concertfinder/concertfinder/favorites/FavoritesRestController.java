package com.concertfinder.concertfinder.favorites;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.ladder.service.ConcertAndFavoritesLadderService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritesRestController {

    private final FavoritesService favoritesService;
    private final ConcertAndFavoritesLadderService concertAndFavoritesLadderService;

    // 콘서트 정보 저장 및 즐겨찾기 저장
    @PostMapping("/{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> addFavorites(@PathVariable @NotBlank(message = "저장 할 콘서트를 찾지 못했습니다!") String concertId
                                                            , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        concertAndFavoritesLadderService.insertFavoritesAndConcert(concertId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 등록 성공"));
    }

    // 즐겨찾기 삭제
    @DeleteMapping("{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFavorites(@PathVariable @NotBlank(message = "삭제 할 콘서트를 찾지 못했습니다!") String concertId
                                                                , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        favoritesService.deleteFavorites(concertId, loginUserDTO.getId());
        
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 삭제 성공"));
    }
}
