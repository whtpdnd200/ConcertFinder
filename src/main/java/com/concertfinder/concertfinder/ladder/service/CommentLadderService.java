package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLadderService {

    private final PostService postService;
    private final CommentService commentService;

    // 댓글 알림 전송 및 댓글 저장
    public void insertComment(long postId,long userId, String comment) {

        String title = postService.getPostTitle(postId);

        long receiverId = postService.getUserIdByPost(postId);

        commentService.insertComment(postId, comment, userId, receiverId, title);
    }
}
