package com.concertfinder.concertfinder.user;

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
    public Map<String, Object> isDuplicate(@RequestParam String userId) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("isDuplicate", userService.isDuplicate(userId));

        return resultMap;
    }

    // 회원가입 : 회원가입 기능
    @PostMapping
    public Map<String, String> createUser(@ModelAttribute JoinUserDTO joinUserDTO) {

        Map<String, String> resultMap = new HashMap<>();
        if(userService.insertUser(joinUserDTO)) {
            resultMap.put("result", "success");
            return resultMap;
        }
        resultMap.put("result", "fail");
        return resultMap;
    }

    // 로그인 : 로그인 기능
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String userId
                                    , @RequestParam String password
                                    , HttpSession session) {

        Map<String, String> resultMap = new HashMap<>();
        LoginUserDTO loginUserDTO = userService.loginUser(userId, password);

        if(loginUserDTO != null) {
            session.setAttribute("userInfo", loginUserDTO);
            resultMap.put("result", "success");

            return resultMap;
        }
        resultMap.put("result", "fail");
        return resultMap;
    }

    // 회원정보 수정 기능
    @PutMapping()
    public Map<String, String> modify(@RequestBody ModifyUserDTO modifyUserDTO
                                     , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        Map<String, String> resultMap = new HashMap<>();
        if(userService.userModify(loginUserDTO.getId(), modifyUserDTO)) {
            session.setAttribute("userInfo", userService.getUser(loginUserDTO.getId()));
            resultMap.put("result", "success");
            return resultMap;
        }

        resultMap.put("result", "fail");
        return resultMap;
    }
}
