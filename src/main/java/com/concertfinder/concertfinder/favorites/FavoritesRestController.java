package com.concertfinder.concertfinder.favorites;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.common.service.BasicService;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritesRestController {

    private final FavoritesService favoritesService;
    private final BasicService basicService;


    @PostMapping("/{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> addFavorites(@PathVariable String concertId
                                                            , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        basicService.insertFavoritesAndConcert(concertId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 등록 성공"));
    }

    @DeleteMapping("{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFavorites(@PathVariable String concertId
                                                                , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        favoritesService.deleteFavorites(concertId, loginUserDTO.getId());
        
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 삭제 성공"));
    }
}
