package com.concertfinder.concertfinder.post;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.post.DTO.PostModifyDTO;
import com.concertfinder.concertfinder.post.DTO.PostWriteDTO;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;


    // 게시글 생성 API
    @PostMapping("/{concertId}")
    public ApiResponseDTO<Void> createPost(@PathVariable String concertId
                                    , @ModelAttribute PostWriteDTO postWriteDTO
                                    , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        if(postService.postInsert(concertId, postWriteDTO, loginUserDTO.getId())) {
            return ApiResponseDTO.success("게시글 작성 성공");
        }

        return ApiResponseDTO.fail("게시글 작성 실패!");
    }

    // 게시글 수정 API
    @PutMapping("/{postId}")
    public ApiResponseDTO<Void> updatePost(@PathVariable long postId
                                          , @RequestBody PostModifyDTO postModifyDTO
                                          , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        if(postService.postUpdate(postId, postModifyDTO, loginUserDTO.getId())) {

            return ApiResponseDTO.success("게시글 수정 성공");
        }

        return ApiResponseDTO.fail("게시글 수정 실패!");
    }

    // 게시글 삭제 API
    @DeleteMapping("{postId}")
    public ApiResponseDTO<Void> removePost(@PathVariable long postId
                                         , HttpSession session) {
        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        if(postService.postDelete(postId, loginUserDTO.getId())) {


            return ApiResponseDTO.success("게시글 삭제 성공");
        }

        return ApiResponseDTO.fail("게시글 삭제 실패!");
    }

    // 게시글 목록 출력 API
    @GetMapping("/{concertId}/list")
    public ApiResponseDTO<Page<PostListDTO>> getPosts(@PathVariable String concertId
                                    , @RequestParam int page
                                    , @RequestParam int size
                                    , @RequestParam char category
                                    , Pageable pageable) {

        Page<PostListDTO> posts = postService.getPosts(concertId, page, size, category, pageable);
        if(posts != null) {
            return ApiResponseDTO.success("목록 출력 성공", posts);
        }
        return ApiResponseDTO.fail("게시글 목록 출력 실패");
    }
}
