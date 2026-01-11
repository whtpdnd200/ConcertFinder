package com.concertfinder.concertfinder.favorites;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.ladder.service.ConcertAndFavoritesLadderService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
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


    @PostMapping("/{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> addFavorites(@PathVariable String concertId
                                                            , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        concertAndFavoritesLadderService.insertFavoritesAndConcert(concertId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 등록 성공"));
    }

    @DeleteMapping("{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFavorites(@PathVariable String concertId
                                                                , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        favoritesService.deleteFavorites(concertId, loginUserDTO.getId());
        
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 삭제 성공"));
    }
}
