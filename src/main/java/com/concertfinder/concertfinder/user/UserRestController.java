package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import com.concertfinder.concertfinder.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 회원가입 : 중복검사 기능
    @GetMapping("/id-check")
    public ResponseEntity<ApiResponseDTO<Boolean>> isDuplicate(@RequestParam
                                                               @NotBlank(message = "아이디는 비어 있을 수 없습니다!")
                                                               String userId) {

        boolean isDuplicate = userService.isDuplicate(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO
                        .isDuplicate("", isDuplicate));
    }

    // 회원가입 : 회원가입 기능
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> createUser(@ModelAttribute @Valid JoinUserDTO joinUserDTO) {

        userService.insertUser(joinUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("회원가입 성공"));
    }

    // 로그인 : 로그인 기능
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<Void>> login(@RequestParam String userId
                                    , @RequestParam String password
                                    , HttpServletResponse response) {

        userService.loginUser(userId, password, response);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("로그인 성공"));
    }

    // 회원정보 수정 기능
    @PutMapping
    public ResponseEntity<ApiResponseDTO<Void>> modify(@RequestBody @Valid ModifyUserDTO modifyUserDTO
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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("회원 정보 수정 성공"));
    }

    // 회원 탈퇴 기능
    // 유저 정보 작성한 게시글, 댓글, 즐겨찾기, 동행 채팅방 1:1 채팅방
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> removeUser(@AuthenticationPrincipal PrincipalDetails principal
                                                            , HttpServletRequest request
                                                           , HttpServletResponse response) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        userService.updateDeleteUser(loginUserDTO.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success(null));
    }
}
