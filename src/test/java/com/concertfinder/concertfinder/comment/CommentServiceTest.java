package com.concertfinder.concertfinder.comment;

import com.concertfinder.concertfinder.comment.DTO.CommentModifyDTO;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("타인의 댓글은 수정 할 수 없음")
    public void updateCommentTest() {

        Long userId = 1l;
        long commentId = 1L;
        CommentModifyDTO modifyDTO = CommentModifyDTO.builder()
                .comment("test")
                .build();

        assertThrows(UnAuthorizedException.class, () -> {
            commentService.updateComment(commentId, userId, modifyDTO);
        });

    }
}
