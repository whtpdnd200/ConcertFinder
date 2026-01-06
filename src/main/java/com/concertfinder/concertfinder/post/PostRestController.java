package com.concertfinder.concertfinder.post;

import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.post.DTO.PostModifyDTO;
import com.concertfinder.concertfinder.post.DTO.PostWriteDTO;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;


    // 게시글 생성 API
    @PostMapping("/{concertId}")
    public Map<String, String> createPost(@PathVariable String concertId
                                    , @ModelAttribute PostWriteDTO postWriteDTO
                                    , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        Map<String, String> resultMap = new HashMap<>();
        if(postService.postInsert(concertId, postWriteDTO, loginUserDTO.getId())) {
            resultMap.put("result", "success");
            return resultMap;
        }
        resultMap.put("result", "fail");
        return resultMap;
    }

    // 게시글 수정 API
    @PutMapping("/{postId}")
    public Map<String, String> updatePost(@PathVariable long postId
                                          , @RequestBody PostModifyDTO postModifyDTO
                                          , HttpSession session) {

        Map<String, String> resultMap = new HashMap<>();
        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        if(postService.postUpdate(postId, postModifyDTO, loginUserDTO.getId())) {

            resultMap.put("result", "success");
            return resultMap;
        }

        resultMap.put("result", "fail");
        return resultMap;
    }

    // 게시글 삭제 API
    @DeleteMapping("{postId}")
    public Map<String, String> removePost(@PathVariable long postId
                                         , HttpSession session) {
        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        Map<String, String> resultMap = new HashMap<>();
        if(postService.postDelete(postId, loginUserDTO.getId())) {

            resultMap.put("result", "success");
            return resultMap;
        }

        resultMap.put("result", "fail");
        return resultMap;
    }

    // 게시글 목록 출력 API
    @GetMapping("/{concertId}/list")
    public Page<PostListDTO> getPosts(@PathVariable String concertId
                                    , @RequestParam int page
                                    , @RequestParam int size
                                    , @RequestParam char category
                                    , Pageable pageable) {
        return postService.getPosts(concertId, page, size, category, pageable);
    }
}
