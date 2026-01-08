package com.concertfinder.concertfinder.comment.service;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.DTO.CommentModifyDTO;
import com.concertfinder.concertfinder.comment.domain.Comment;
import com.concertfinder.concertfinder.comment.repository.CommentRepository;
import com.concertfinder.concertfinder.exceptionHandler.GlobalExceptionHandler;
import com.concertfinder.concertfinder.exceptionHandler.customException.UnAuthorizedException;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    // 댓글 저장 메서드
    public void insertComment(long postId, String comment, Long userId) {

        GlobalExceptionHandler.loginException(userId);

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
            throw new RuntimeException("서버 에러로 인해 댓글 작성이 실패 했습니다 잠시 후 다시 시도해주세요!");
        }
    }

    // 댓글 수정 메서드
    public void updateComment(long commentId, Long userId, CommentModifyDTO commentModifyDTO) {

        GlobalExceptionHandler.loginException(userId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            if(!userId.equals(comment.getUserId())) {
                throw new UnAuthorizedException("타인의 댓글은 수정 할 수 없습니다!");
            }
            comment = comment.toBuilder()
                    .comment(commentModifyDTO.getComment())
                    .build();

            try {
                commentRepository.save(comment);
            } catch(DataAccessException e) {
                throw new RuntimeException("서버 에러로 인해 댓글 수정이 실패 했습니다 잠시 후 다시 시도해주세요!");
            }
        }
    }

    // 댓글 목록 출력 메서드
    public Page<CommentListDTO> getCommentList(long postId, int page, int size, Pageable pageable) {


        Page<Comment> comments = commentRepository.findAllByPostId(postId, PageRequest.of(page, size, Sort.by("id").descending()));

        if(comments == null) {
            throw new NoSuchElementException("댓글 목록 조회에 실패 했습니다! 나중에 다시 시도 해주세요");
        }
        List<CommentListDTO> lists = new ArrayList<>();

        for(Comment comment : comments) {
            CommentListDTO commentListDTO = CommentListDTO.builder()
                    .id(comment.getId())
                    .postId(comment.getPostId())
                    .userId(comment.getUserId())
                    .userNickname(userService.getNickname(comment.getUserId()))
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .build();

            lists.add(commentListDTO);
        }

        Page<CommentListDTO> commentList = new PageImpl<>(lists, pageable, commentRepository.countByPostId(postId));
        return commentList;
    }

    // 댓글 목록 갯수 반환 메서드
    public int getCommentCount(long postId) {
        return commentRepository.countByPostId(postId);
    }

    // 댓글 삭제 메서드
    public void deleteComment(long commentId, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if(!userId.equals(comment.getUserId())) {
                throw new UnAuthorizedException("타인의 댓글은 삭제 할 수 없습니다!");
            }

            try {
                commentRepository.delete(comment);
            } catch(DataAccessException e) {
                throw new RuntimeException("서버 에러로 인해 댓글 삭제가 실패 했습니다 잠시 후 다시 시도 해주세요!");
            }
        }
    }
}
