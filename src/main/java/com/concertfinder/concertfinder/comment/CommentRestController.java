package com.concertfinder.concertfinder.comment;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponseDTO<Object>> createComment(@PathVariable long postId
                                             , @RequestParam String comment
                                             , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        if(loginUserDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDTO.fail("로그인이 필요한 서비스 입니다!"));
        }

        try {
            commentService.insertComment(postId, comment, loginUserDTO.getId());
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.fail(e.getMessage()));
        }
        catch(HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.fail("댓글 작성 에러!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("댓글 작성 성공"));
    }

    // 댓글 목록 출력 API
    @GetMapping("/{postId}/list")
    public ResponseEntity<ApiResponseDTO<Page<CommentListDTO>>> getComments(@PathVariable long postId
                                                                           , @RequestParam int page
                                                                           , @RequestParam int size
                                                                           , Pageable pageable) {
        Page<CommentListDTO> commentList = commentService.getCommentList(postId, page, size, pageable);
        if(commentList == null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.fail("댓글 목록을 불러오지 못했습니다!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 목록 출력 성공", commentList));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO<Object>> removeComment(@PathVariable long commentId
                                                             , HttpSession session) {
        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");
        if(loginUserDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDTO.fail("로그인이 필요한 서비스 입니다!"));
        }

        try {
            commentService.deleteComment(commentId, loginUserDTO.getId());

        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.fail(e.getMessage()));
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.fail(e.getMessage()));
        }


        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 삭제 성공"));
    }
}
