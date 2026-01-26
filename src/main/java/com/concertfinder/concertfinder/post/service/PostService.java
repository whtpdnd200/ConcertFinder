package com.concertfinder.concertfinder.post.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.exception.GlobalExceptionHandler;
import com.concertfinder.concertfinder.exception.custom_exception.NotFoundException;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
import com.concertfinder.concertfinder.post.DTO.PostDetailDTO;
import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.post.DTO.PostModifyDTO;
import com.concertfinder.concertfinder.post.DTO.PostWriteDTO;
import com.concertfinder.concertfinder.post.domain.Post;
import com.concertfinder.concertfinder.post.repository.PostRepository;
import com.concertfinder.concertfinder.user.service.UserService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final CommentService commentService;

    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;

    // 게시글 DTO에 담기
    @Transactional
    public PostDetailDTO addDto(Post post, long userId) {

        AccompanyInfoDTO accompanyInfoDTO = null;

        if(post.getCategory().equals('R')) {
            accompanyInfoDTO = accompanyAndAccompanyCountLadderService.getAccompanyInfo(post.getId(), userId);
        }

        PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                .id(post.getId())
                .concertId(post.getConcertId())
                .userId(post.getUserId())
                .userNickname(userService.getNickname(post.getUserId()))
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCount(commentService.getCommentCount(post.getId()))
                .isUserDelete(userService.getIsDelete(post.getUserId()))
                .accompanyInfoDTO(accompanyInfoDTO)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return postDetailDTO;
    }

    // 게시글 DB 저장
    @Transactional
    public void postInsert(String concertId
                             , PostWriteDTO postWriteDTO
                             , Long userId) {

        GlobalExceptionHandler.loginException(userId);

        Post post = Post.builder()
                .concertId(concertId)
                .userId(userId)
                .category(postWriteDTO.getCategory())
                .title(postWriteDTO.getTitle())
                .content(postWriteDTO.getContent())
                .build();

        try {
            Post postEntity = postRepository.save(post);
            if(postEntity.getCategory().equals('R')) {
                AccompanyAddDTO accompanyAddDTO = AccompanyAddDTO.builder()
                        .postId(postEntity.getId())
                        .userId(userId)
                        .headCount(postWriteDTO.getHeadCount())
                        .place(postWriteDTO.getPlace())
                        .sDateTime(postWriteDTO.getSDateTime())
                        .build();

                accompanyAndAccompanyCountLadderService.insertAccompanyAndAccompanyCount(accompanyAddDTO, userId , postWriteDTO.getTitle());
            }
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 인해 게시글 작성이 실패 하였습니다 잠시 후 다시 시도해주세요!");
        }

    }

    // 특정 게시글 조회
    public PostDetailDTO getPost(long postId, long userId) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(!optionalPost.isPresent()) {

            throw new NotFoundException("존재하지 않는 게시글 입니다!");
        }

        PostDetailDTO postDetailDTO = addDto(optionalPost.get(), userId);
        return postDetailDTO;
    }

    // 게시글 수정 메서드
    public void postUpdate(long postId, PostModifyDTO postModifyDTO, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if(!userId.equals(post.getUserId())) {
                throw new UnAuthorizedException("다른 사람의 게시글은 수정 할 수 없습니다!");
            }
            post = post.toBuilder()
                    .category(postModifyDTO.getCategory())
                    .title(postModifyDTO.getTitle())
                    .content(postModifyDTO.getContent())
                    .build();

            try {

                postRepository.save(post);
            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러로 게시글을 수정 하지 못했습니다 잠시 후 다시 시도 해주세요!");
            }
        }
    }

    // 게시글 PK로 삭제 메서드
    @Transactional
    public void postDelete(long postId, Long userId) {

        GlobalExceptionHandler.loginException(userId);

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if(!userId.equals(post.getUserId())) {
                throw new UnAuthorizedException("다른 사람의 게시글은 삭제 할 수 없습니다!");
            }
            try {
                postRepository.delete(post);
                commentService.deleteAllComment(postId);
                if(post.getCategory().equals('R')) {

                    accompanyAndAccompanyCountLadderService.deleteAccompanyAndAccompanyCount(postId);
                }
            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러로 인해 게시글 삭제가 실패 하였습니다 잠시 후 다시 시도해주세요!");
            }
        }
    }

    // accompanyId로 게시글 PK 얻어와서 삭제
    @Transactional
    public void postDeleteByAccompanyId(long accompanyId, Long userId, Long roomId) {

        GlobalExceptionHandler.loginException(userId);

        long postId = accompanyAndAccompanyCountLadderService.getPostId(accompanyId);

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if(!userId.equals(post.getUserId())) {

                throw new UnAuthorizedException("다른 사람의 게시글은 삭제 할 수 없습니다!");
            }

            try {

                postRepository.delete(post);
                commentService.deleteAllComment(postId);

                if(post.getCategory().equals('R')) {

                    accompanyAndAccompanyCountLadderService.deleteAccompanyAndAccompanyCountByAccompanyId(accompanyId, roomId, userId);
                }

            } catch(DataAccessException e) {

                throw new RuntimeException("서버 에러로 인해 게시글 삭제가 실패 하였습니다 잠시 후 다시 시도해주세요!");
            }
        }
    }

    // 게시글 목록 조회 메서드
    public Page<PostListDTO> getPosts(String concertId, int page, int size, char category, Pageable pageable) {

        Page<Post> posts = null;
        Long count = null;
        if(category =='A') {
            posts = postRepository
                     .findAllByConcertId(concertId
                             , PageRequest.of(page, size, Sort.by("id").descending()));
            count = postRepository.countByConcertId(concertId);
        } else {
            posts = postRepository
                    .findAllByConcertIdAndCategory(concertId
                                                   , category
                                                   , PageRequest.of(page, size, Sort.by("id").descending()));
            count = postRepository.countByConcertIdAndCategory(concertId, category);
        }

        if(posts == null) {
            throw new NoSuchElementException("게시글 목록 조회에 실패 했습니다! 나중에 다시 시도 해주세요!");
        }

        List<PostListDTO> postList = new ArrayList<>();

        for(Post post : posts) {

            PostListDTO postListDTO = PostListDTO.builder()
                    .id(post.getId())
                    .userId(post.getUserId())
                    .category(post.getCategory())
                    .title(post.getTitle())
                    .userNickname(userService.getNickname(post.getUserId()))
                    .createdAt(post.getCreatedAt())
                    .build();

            postList.add(postListDTO);
        }
        Page<PostListDTO> postPageList = new PageImpl<>(postList, pageable, count);

        return postPageList;
    }

    // 탈퇴한 사용자의 게시글 삭제
    public void deleteUserPost(List<Long> userIdList) {

        List<Post> posts = postRepository.findAllByUserIdIn(userIdList);

        if(!posts.isEmpty()) {

            for(Post p : posts) {

                if(userService.getIsDelete(p.getUserId())) {

                    postDelete(p.getId(), p.getUserId());
                }
            }
        }
    }
}
