package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 회원가입 : 중복검사 기능
    @GetMapping("/id-check")
    public ResponseEntity<ApiResponseDTO<Boolean>> isDuplicate(@RequestParam String userId) {

        boolean isDuplicate = userService.isDuplicate(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO
                        .isDuplicate("", isDuplicate));
    }

    // 회원가입 : 회원가입 기능
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> createUser(@ModelAttribute JoinUserDTO joinUserDTO) {

        userService.insertUser(joinUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("회원가입 성공"));
    }

    // 로그인 : 로그인 기능
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponseDTO<Void>> login(@RequestParam String userId
//                                    , @RequestParam String password
//                                    , HttpSession session) {
//
//        LoginUserDTO loginUserDTO = userService.loginUser(userId, password);
//
//        session.setAttribute("userInfo", loginUserDTO);
//        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("로그인 성공"));
//    }

    // 회원정보 수정 기능
    @PutMapping()
    public ResponseEntity<ApiResponseDTO<Void>> modify(@RequestBody ModifyUserDTO modifyUserDTO
                                     , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        userService.userModify(loginUserDTO.getId(), modifyUserDTO);

        // 업데이트 된 유저 정보 새로 조회
        LoginUserDTO updatedDTO = userService.getUser(loginUserDTO.getId());

        // 스프링 시큐리티에 넣을 객체 새로 생성
        PrincipalDetails updatedPrincipal = new PrincipalDetails(updatedDTO, null);

        // 유저 객체와 인증 정보 저장 할 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                updatedPrincipal
                , null
                , updatedPrincipal.getAuthorities());

        // 스프링 시큐리티의 세션 같은곳에 정보 업데이트
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // session.setAttribute("userInfo", userService.getUser(loginUserDTO.getId()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("회원 정보 수정 성공"));
    }
}
