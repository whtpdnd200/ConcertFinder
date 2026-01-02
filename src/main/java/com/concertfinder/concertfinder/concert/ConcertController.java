package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.concert.DTO.ResponsesDTO;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final SidoCodeService sidoCodeService;

    @GetMapping("/list")
    public String list(Model model
                       , Integer page
                       , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        model.addAttribute("concertList", concertService.getList(loginUserDTO.getAttentionAreaCode(), page));
        return "concertfinder/concert/list";
    }


}
