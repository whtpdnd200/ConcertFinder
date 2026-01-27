package com.concertfinder.concertfinder.comment.service;

import com.concertfinder.concertfinder.comment.DTO.CommentListDTO;
import com.concertfinder.concertfinder.comment.DTO.CommentModifyDTO;
import com.concertfinder.concertfinder.comment.domain.Comment;
import com.concertfinder.concertfinder.comment.repository.CommentRepository;
import com.concertfinder.concertfinder.exception.GlobalExceptionHandler;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private static final String COMMENT_LIST_PREFIX = "comment:list:";

    // 댓글 저장 메서드
    public void insertComment(long postId, String comment, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        String key = COMMENT_LIST_PREFIX + postId;

        Comment commentEntity = Comment.builder()
                .postId(postId)
                .userId(userId)
                .comment(comment)
                .build();

        try {
            commentRepository.save(commentEntity);
            redisTemplate.delete(key);
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

            String key = COMMENT_LIST_PREFIX + comment.getPostId();

            comment = comment.toBuilder()
                    .comment(commentModifyDTO.getComment())
                    .build();

            try {
                commentRepository.save(comment);
                redisTemplate.delete(key);
            } catch(DataAccessException e) {
                throw new RuntimeException("서버 에러로 인해 댓글 수정이 실패 했습니다 잠시 후 다시 시도해주세요!");
            }
        }
    }

    // 댓글 목록 첫 페이지 출력 메서드
    public Page<CommentListDTO> getFirstCommentList(long postId, int page, int size, Pageable pageable) {

        if(page == 0) {

            String key = COMMENT_LIST_PREFIX + postId;

            Object cacheComments = redisTemplate.opsForValue().get(key);

            if(cacheComments != null) {

                log.info("redis Cache Hit");

                List<CommentListDTO> cacheCommentList = objectMapper.convertValue(cacheComments, new TypeReference<List<CommentListDTO>>() {});

                cacheCommentList.forEach(dto -> dto.setUserNickname(userService.getNickname(dto.getUserId())));

                log.info("댓글 목록 : {} ", cacheCommentList);

                Page<CommentListDTO> pageCommentList = new PageImpl<>(cacheCommentList, pageable, commentRepository.countByPostId(postId));

                return pageCommentList;
            }

            log.info("redis Cache Miss");

            Page<CommentListDTO> pageComments = getCommentList(postId, page, size, pageable);

            List<CommentListDTO> comments = new ArrayList<>(pageComments.getContent());

            //comments.forEach(dto -> dto.setUserNickname(null));

            redisTemplate.opsForValue().set(key, comments, java.time.Duration.ofMinutes(10));

            return pageComments;
        }

        return getCommentList(postId, page, size, pageable);
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

    // 게시글의 댓글 목록 아이디 반환
    public List<Long> getCommentIdList(long postId) {

        List<Comment> comments = commentRepository.findAllByPostId(postId);

        List<Long> commentIdList = new ArrayList<>();

        for(Comment c : comments) {

            commentIdList.add(c.getId());
        }

        return commentIdList;
    }

    // 게시글에 포함 된 댓글 전체 삭제
    public void deleteAllComment(long postId) {

        List<Long> commentIdList = getCommentIdList(postId);

        for(Long id : commentIdList) {

            deleteComment(id);
        }
    }

    public void deleteComment(long commentId) {


        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            try {
                commentRepository.delete(comment);
            } catch(DataAccessException e) {
                throw new RuntimeException("서버 에러로 인해 댓글 삭제가 실패 했습니다 잠시 후 다시 시도 해주세요!");
            }
        }
    }

    // 댓글 삭제 메서드
    public void deleteComment(long commentId, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            String key = COMMENT_LIST_PREFIX + comment.getPostId();

            if(!userId.equals(comment.getUserId())) {
                throw new UnAuthorizedException("타인의 댓글은 삭제 할 수 없습니다!");
            }

            try {
                commentRepository.delete(comment);
                redisTemplate.delete(key);
            } catch(DataAccessException e) {
                throw new RuntimeException("서버 에러로 인해 댓글 삭제가 실패 했습니다 잠시 후 다시 시도 해주세요!");
            }
        }
    }

    public void deleteUserComment() {

        List<Comment> comments = commentRepository.findAll();

        if(!comments.isEmpty()) {
            for(Comment c : comments) {

                if(!userService.isExistsUser(c.getUserId())) {

                    deleteComment(c.getId());
                }
            }
        }
    }

    public void deleteUserComment(List<Long> userIdList) {

        try {

            commentRepository.deleteAllByUserIdIn(userIdList);

        } catch(DataAccessException e) {

            throw new RuntimeException("삭제 에러");
        }
    }
}
