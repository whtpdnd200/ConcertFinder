package com.concertfinder.concertfinder.concert;

import com.concertfinder.concertfinder.sidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;
    private final SidoCodeService sidoCodeService;

    // 콘서트 목록 페이지
    @GetMapping("/list")
    public String list(Model model
                       , Integer page
                       , @AuthenticationPrincipal PrincipalDetails principal
                       , @RequestParam(required = false) String keyword
                       , @RequestParam(name = "code", required = false) String areaCode) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        model.addAttribute("concertList", concertService.getList(loginUserDTO.getAttentionAreaCode()
                                                                                    , page
                                                                                    , areaCode
                                                                                    , keyword
                                                                                    , loginUserDTO.getId()));
        model.addAttribute("areaCode", areaCode);
        model.addAttribute("areaName", sidoCodeService.getSidoName(areaCode));
        model.addAttribute("keyword", keyword);
        return "concertfinder/concert/list";
    }

    // 콘서트 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable String id
                         , @AuthenticationPrincipal PrincipalDetails principal
                         , Model model) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        model.addAttribute("concertInfo", concertService.getConcertInfo(id, loginUserDTO.getId()).getInfoDTO());
        return "concertfinder/concert/detail";
    }
}
