package com.concertfinder.concertfinder.post.service;

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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

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
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return postDetailDTO;
    }

    // 게시글 DB 저장
    public boolean postInsert(String concertId
                             , PostWriteDTO postWriteDTO
                             , long userId) {

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
            return false;
        }

        return true;
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
    public boolean postUpdate(long postId, PostModifyDTO postModifyDTO, long userId) {


        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if(userId != post.getUserId()) {
                return false;
            }
            post = post.toBuilder()
                    .category(postModifyDTO.getCategory())
                    .title(postModifyDTO.getTitle())
                    .content(postModifyDTO.getContent())
                    .build();

            try {
                postRepository.save(post);
            } catch(DataAccessException e) {
                return false;
            }
        }

        return true;
    }

    // 게시글 삭제 메서드
    public boolean postDelete(long postId, long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if(post.getUserId() != userId) {
                return false;
            }
            try {
                postRepository.delete(post);
            } catch(DataAccessException e) {
                return false;
            }
        }
        return true;
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
