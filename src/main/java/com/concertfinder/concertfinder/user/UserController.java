package com.concertfinder.concertfinder.user;

import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.jwt.CookieUtil;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
import com.concertfinder.concertfinder.sidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SidoCodeService sidoCodeService;
    private final ConcertService concertService;
    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 회원가입 페이지
    @GetMapping("/join")
    public String join(Authentication authentication
                       , Model model) {

        if(authentication != null && authentication.isAuthenticated()) {
            return "redirect:/concert/list";
        }


        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        return "concertfinder/user/join";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(Authentication authentication) {

        // 유저 인증 정보 객체가 있고 인증된 유저라면 페이지 강제 이동
        if(authentication != null && authentication.isAuthenticated()) {
            return "redirect:/concert/list";
        }

        return "concertfinder/user/login";
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String myPage(Model model
                        , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        model.addAttribute("chatRoomList", accompanyAndAccompanyCountLadderService.getTop3ChatRoomList(loginUserDTO.getId()));
        model.addAttribute("concertList", concertService.getConcertListTop3(loginUserDTO.getId()));
        return "concertfinder/user/mypage";
    }

    // 로그아웃 기능
    @GetMapping("/logout")
    public String logout(HttpServletRequest request
                        , HttpServletResponse response
                        , @AuthenticationPrincipal PrincipalDetails principal) {

        CookieUtil.deleteCookie(request, response, "ACCESS_TOKEN");
        CookieUtil.deleteCookie(request, response, "REFRESH_TOKEN");
        CookieUtil.deleteCookie(request, response, "XSRF_TOKEN");

        SecurityContextHolder.clearContext();

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        redisTemplate.delete("user:info:" + loginUserDTO.getUserId());
        redisTemplate.delete("refreshToken:" + loginUserDTO.getUserId());

        return "redirect:/user/login";
    }

    // 회원정보 수정 페이지
    @GetMapping("/modify")
    public String modify(Model model) {

        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        return "concertfinder/user/modify";
    }
}
