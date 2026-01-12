package com.concertfinder.concertfinder.post.service;

import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.exception.GlobalExceptionHandler;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.post.DTO.PostDetailDTO;
import com.concertfinder.concertfinder.post.DTO.PostListDTO;
import com.concertfinder.concertfinder.post.DTO.PostModifyDTO;
import com.concertfinder.concertfinder.post.DTO.PostWriteDTO;
import com.concertfinder.concertfinder.post.domain.Post;
import com.concertfinder.concertfinder.post.repository.PostRepository;
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
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final CommentService commentService;


    // 게시글 DTO에 담기
    public PostDetailDTO addDto(Post post) {
        PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                .id(post.getId())
                .concertId(post.getConcertId())
                .userId(post.getUserId())
                .userNickname(userService.getNickname(post.getUserId()))
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCount(commentService.getCommentCount(post.getId()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return postDetailDTO;
    }

    // 게시글 DB 저장
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
            postRepository.save(post);
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 인해 게시글 작성이 실패 하였습니다 잠시 후 다시 시도해주세요!");
        }

    }

    // 특정 게시글 조회
    public PostDetailDTO getPost(long postId) {

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            PostDetailDTO postDetailDTO = addDto(optionalPost.get());
            return postDetailDTO;
        }

        return null;
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

    // 게시글 삭제 메서드
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
            throw new NoSuchElementException("게시글 목록 조회에 실패 했습니다! 나중에 다시 시도 해주세요");
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
}
