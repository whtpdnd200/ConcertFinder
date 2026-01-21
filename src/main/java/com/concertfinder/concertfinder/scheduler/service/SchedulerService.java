package com.concertfinder.concertfinder.scheduler.service;

import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.concert.domain.Concert;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.ladder.service.ChatRoomAndChatRoomAndUserLadderService;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.review.service.ReviewService;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerService {

    private final ConcertService concertService;
    private final FavoritesService favoritesService;
    private final ChatRoomAndChatRoomAndUserLadderService chatRoomAndChatRoomAndUserLadderService;
    private final PostService postService;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Transactional
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void concertDeleteByCron() {

        List<Concert> concerts = concertService.getConcertList();

        for(Concert concert : concerts) {

            if(!favoritesService.isFavorites(concert.getConcertId())
                    && !concert.getState().equals("공연중")
                    && !concert.getState().equals("공연예정")) {

                concertService.deleteConcert(concert);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "*/1 * * * * *", zone = "Asia/Seoul")
    public void userRelationDelete() {

        List<Long> userIdList = userService.getDeleteUserIdList();

        commentService.deleteUserComment(userIdList);
        favoritesService.deleteUserFavorites(userIdList);
        reviewService.deleteUserReview(userIdList);
        chatRoomAndChatRoomAndUserLadderService.deleteChatRoomUser(userIdList);
        postService.deleteUserPost(userIdList);
        userService.deleteUsers(userIdList);
    }
}
