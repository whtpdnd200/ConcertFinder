package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.service.PrincipalDetailsService;
import com.concertfinder.concertfinder.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 회원가입 : 중복검사 기능
    @GetMapping("/id-check")
    public ResponseEntity<ApiResponseDTO<Boolean>> isDuplicate(@RequestParam String userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO
                        .isDuplicate("", userService.isDuplicate(userId)));
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
                                     , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        userService.userModify(loginUserDTO.getId(), modifyUserDTO);
        session.setAttribute("userInfo", userService.getUser(loginUserDTO.getId()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("회원 정보 수정 성공"));
    }
}
