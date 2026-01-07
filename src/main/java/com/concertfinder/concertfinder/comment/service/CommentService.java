package com.concertfinder.concertfinder.comment.service;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.DTO.CommentWriteDTO;
import com.concertfinder.concertfinder.comment.domain.Comment;
import com.concertfinder.concertfinder.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 저장 메서드
    public void insertComment(long postId, String comment, long userId) {

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다!");
        }

        Comment commentEntity = Comment.builder()
                .postId(postId)
                .userId(userId)
                .comment(comment)
                .build();

        try {
            commentRepository.save(commentEntity);
        } catch(DataAccessException e) {
            throw new RuntimeException("댓글 작성 에러!");
        }

    }

    // 댓글 목록 출력 메서드
    public Page<CommentListDTO> getCommentList(long postId, Pageable pageable) {


        return null;
    }
}
