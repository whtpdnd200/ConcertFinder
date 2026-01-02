package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final SidoCodeService sidoCodeService;

    @GetMapping("/list")
    public String list(Model model
                       , Integer page
                       , HttpSession session
                       , @RequestParam(required = false) String keyword
                       , @RequestParam(name = "code", required = false) String areaCode) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        model.addAttribute("concertList", concertService.getList(loginUserDTO.getAttentionAreaCode()
                                                                                    , page
                                                                                    , areaCode
                                                                                    , keyword));
        model.addAttribute("areaCode", areaCode);
        model.addAttribute("areaName", sidoCodeService.getSidoName(areaCode));
        model.addAttribute("keyword", keyword);
        return "concertfinder/concert/list";
    }

    @GetMapping("/detail")
    public String detail() {
        return "concertfinder/concert/detail";
    }
}
