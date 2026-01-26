package com.concertfinder.concertfinder.post;

import com.concertfinder.concertfinder.post.DTO.PostDetailDTO;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 글 작성 페이지
    @GetMapping("/write/{concertId}")
    public String write(@PathVariable String concertId
                        , Model model) {

        model.addAttribute("concertId", concertId);
        return "concertfinder/post/write";
    }

    // 글 상세 페이지
    @GetMapping("/{postId}")
    public String detail(@PathVariable long postId
                         , @AuthenticationPrincipal PrincipalDetails principal
                         , Model model) {



        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        PostDetailDTO postDetailDTO = postService.getPost(postId, loginUserDTO.getId());

        model.addAttribute("postInfo", postDetailDTO);
        return "concertfinder/post/detail";
    }

    // 게시글 수정 페이지
    @GetMapping("/modify/{postId}")
    public String modify(@PathVariable long postId
            , @AuthenticationPrincipal PrincipalDetails principal
            , Model model) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        model.addAttribute("postModifyInfo", postService.getPost(postId, loginUserDTO.getId()));
        return "concertfinder/post/modify";
    }
}
