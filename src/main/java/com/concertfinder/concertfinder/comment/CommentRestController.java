package com.concertfinder.concertfinder.comment;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.DTO.CommentModifyDTO;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.CommentLadderService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    private final CommentLadderService commentLadderService;

    // 댓글 작성 API
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponseDTO<Void>> createComment(@PathVariable @NotNull(message = "작성 할 게시글이 존재 하지 않습니다!") Long postId
                                             , @RequestParam String comment
                                             , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();


        commentLadderService.insertComment(postId, loginUserDTO.getId(), comment);
        //commentService.insertComment(postId, comment, loginUserDTO.getId(), receiverId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("댓글 작성 성공"));
    }

    // 댓글 목록 출력 API
    @GetMapping("/{postId}/list")
    public ResponseEntity<ApiResponseDTO<Page<CommentListDTO>>> getComments(@PathVariable long postId
                                                                           , @RequestParam int page
                                                                           , @RequestParam int size
                                                                           , Pageable pageable) {
        Page<CommentListDTO> commentList = commentService.getFirstCommentList(postId, page, size, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("댓글 목록 출력 성공", commentList));
    }

    // 댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO<Void>> modifyComment(@PathVariable @NotNull(message = "수정 할 댓글이 존재 하지 않습니다!") Long commentId
                                                              , @RequestBody CommentModifyDTO comment
                                                              , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        commentService.updateComment(commentId, loginUserDTO.getId(), comment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("댓글 수정 성공"));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeComment(@PathVariable @NotNull(message = "삭제 할 댓글이 존재 하지않습니다!") Long commentId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        commentService.deleteComment(commentId, loginUserDTO.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success(null));
    }
}
