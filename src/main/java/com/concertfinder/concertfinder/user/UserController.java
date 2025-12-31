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

    @GetMapping("/test")
    @ResponseBody
    public List<SidoDTO> test() {

        return sidoCodeService.getAllCode();
    }

    @GetMapping("/join")
    public String join(Model model) {

        model.addAttribute("sidoList", sidoCodeService.getAllCode());
        return "concertfinder/user/join";
    }
}
