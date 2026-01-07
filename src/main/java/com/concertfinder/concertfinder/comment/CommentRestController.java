package com.concertfinder.concertfinder.comment;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.DTO.CommentModifyDTO;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<Void>> createComment(@PathVariable long postId
                                             , @RequestParam String comment
                                             , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        commentService.insertComment(postId, comment, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("댓글 작성 성공"));
    }

    // 댓글 목록 출력 API
    @GetMapping("/{postId}/list")
    public ResponseEntity<ApiResponseDTO<Page<CommentListDTO>>> getComments(@PathVariable long postId
                                                                           , @RequestParam int page
                                                                           , @RequestParam int size
                                                                           , Pageable pageable) {
        Page<CommentListDTO> commentList = commentService.getCommentList(postId, page, size, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 목록 출력 성공", commentList));
    }

    // 댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO<Void>> modifyComment(@PathVariable long commentId
                                                              , @RequestBody CommentModifyDTO comment
                                                              , HttpSession session) {

        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        commentService.updateComment(commentId, loginUserDTO.getId(), comment);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 수정 성공"));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeComment(@PathVariable long commentId
                                                             , HttpSession session) {
        LoginUserDTO loginUserDTO = (LoginUserDTO)session.getAttribute("userInfo");

        commentService.deleteComment(commentId, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 삭제 성공"));
    }
}
