package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 회원가입 : 중복검사 기능
    @GetMapping("/id-check")
    public ApiResponseDTO<Boolean> isDuplicate(@RequestParam String userId) {

        return ApiResponseDTO.isDuplicate("", userService.isDuplicate(userId));
    }

    // 회원가입 : 회원가입 기능
    @PostMapping
    public ApiResponseDTO<Void> createUser(@ModelAttribute JoinUserDTO joinUserDTO) {

        if(userService.insertUser(joinUserDTO)) {
            return ApiResponseDTO.success("회원가입 성공", null);
        }

        return ApiResponseDTO.fail("회원가입 실패!");
    }

    // 로그인 : 로그인 기능
    @PostMapping("/login")
    public ApiResponseDTO<Void> login(@RequestParam String userId
                                    , @RequestParam String password
                                    , HttpSession session) {

        LoginUserDTO loginUserDTO = userService.loginUser(userId, password);

        if(loginUserDTO != null) {
            session.setAttribute("userInfo", loginUserDTO);
            return ApiResponseDTO.success("로그인 성공", null);
        }
        return ApiResponseDTO.fail("로그인 실패! 아이디 혹은 비밀번호를 확인 해주세요!");
    }

    // 회원정보 수정 기능
    @PutMapping()
    public ApiResponseDTO<Void> modify(@RequestBody ModifyUserDTO modifyUserDTO
                                     , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        if(userService.userModify(loginUserDTO.getId(), modifyUserDTO)) {
            session.setAttribute("userInfo", userService.getUser(loginUserDTO.getId()));
            return ApiResponseDTO.success("정보 수정 성공", null);
        }

        return ApiResponseDTO.fail("회원 정보 수정 실패!");
    }
}
