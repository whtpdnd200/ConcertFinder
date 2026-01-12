package com.concertfinder.concertfinder.post;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.post.DTO.PostModifyDTO;
import com.concertfinder.concertfinder.post.DTO.PostWriteDTO;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;


    // 게시글 생성 API
    @PostMapping("/{concertId}")
    public ResponseEntity<ApiResponseDTO<Void>> createPost(@PathVariable String concertId
                                    , @ModelAttribute @Valid PostWriteDTO postWriteDTO
                                    , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        postService.postInsert(concertId, postWriteDTO, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("게시글 작성 성공"));
    }

    // 게시글 수정 API
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponseDTO<Void>> updatePost(@PathVariable @NotNull(message = "수정 할 게시글이 존재 하지 않습니다!") Long postId
                                          , @RequestBody @Valid PostModifyDTO postModifyDTO
                                          , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        postService.postUpdate(postId, postModifyDTO, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("게시글 수정 성공"));
    }

    // 게시글 삭제 API
    @DeleteMapping("{postId}")
    public ResponseEntity<ApiResponseDTO<Void>> removePost(@PathVariable @NotNull(message = "삭제 할 게시글이 존재 하지 않습니다!") Long postId
                                         , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        postService.postDelete(postId, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("게시글 삭제 성공"));
    }

    // 게시글 목록 출력 API
    @GetMapping("/{concertId}/list")
    public ResponseEntity<ApiResponseDTO<Page<PostListDTO>>> getPosts(@PathVariable String concertId
                                    , @RequestParam int page
                                    , @RequestParam int size
                                    , @RequestParam char category
                                    , Pageable pageable) {

        Page<PostListDTO> posts = postService.getPosts(concertId, page, size, category, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("게시글 목록 조회 성공", posts));
    }
}
