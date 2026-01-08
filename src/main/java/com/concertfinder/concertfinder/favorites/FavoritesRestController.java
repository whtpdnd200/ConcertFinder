package com.concertfinder.concertfinder.favorites;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoritesRestController {

    private final FavoritesService FavoritesService;


    @PostMapping("/{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> addFavorites(@PathVariable String concertId
                                                            , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        FavoritesService.addFavorites(concertId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("즐겨찾기 등록 성공"));
    }
}
