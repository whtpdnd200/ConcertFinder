package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.SidoCode.DTO.SidoDTO;
import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
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
    public String myPage() {

        return "concertfinder/user/mypage";
    }
}
