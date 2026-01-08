package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.SidoCode.DTO.SidoDTO;
import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SidoCodeService sidoCodeService;
    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/join")
    public String join(Model model) {

        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        return "concertfinder/user/join";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {

        return "concertfinder/user/login";
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String myPage(Model model
                        , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        model.addAttribute("concertList", userService.getConcertListTop3(loginUserDTO.getId()));
        return "concertfinder/user/mypage";
    }

    // 로그아웃 기능
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/user/login";
    }

    // 회원정보 수정 페이지
    @GetMapping("/modify")
    public String modify(Model model) {

        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        return "concertfinder/user/modify";
    }
}
