package com.concertfinder.concertfinder.comment;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
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
        if(commentService.insertComment(postId, comment, loginUserDTO.getId())) {
            return ResponseEntity.ok(ApiResponseDTO.success("댓글 작성 성공"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.fail("댓글 작성 실패"));
    }

    // 댓글 목록 출력 API
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDTO<Page<CommentListDTO>>> getComments(@PathVariable long postId,
                                                                            Pageable pageable) {

        return null;
    }
}
