package com.concertfinder.concertfinder.scheduler.service;

import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.service.AccompanyService;
import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCount;
import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import com.concertfinder.concertfinder.chat_room.service.ChatRoomService;
import com.concertfinder.concertfinder.chat_room_and_user.domain.ChatRoomAndUser;
import com.concertfinder.concertfinder.chat_room_and_user.service.ChatRoomAndUserService;
import com.concertfinder.concertfinder.comment.service.CommentService;
import com.concertfinder.concertfinder.concert.domain.Concert;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.favorites.domain.Favorites;
import com.concertfinder.concertfinder.favorites.service.FavoritesService;
import com.concertfinder.concertfinder.kafka.DTO.NoticeDTO;
import com.concertfinder.concertfinder.kafka.service.ProducerService;
import com.concertfinder.concertfinder.ladder.service.ChatRoomAndChatRoomAndUserLadderService;
import com.concertfinder.concertfinder.notice.service.NoticeService;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.review.service.ReviewService;
import com.concertfinder.concertfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final ProducerService producerService;
    private final AccompanyService accompanyService;
    private final AccompanyCountService accompanyCountService;
    private final ChatRoomService chatRoomService;
    private final NoticeService noticeService;

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
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void userRelationDelete() {

        List<Long> userIdList = userService.getDeleteUserIdList();

        commentService.deleteUserComment(userIdList);
        favoritesService.deleteUserFavorites(userIdList);
        reviewService.deleteUserReview(userIdList);
        chatRoomAndChatRoomAndUserLadderService.deleteChatRoomUser(userIdList);
        postService.deleteUserPost(userIdList);
        userService.deleteUsers(userIdList);
    }

    @Transactional
    @Scheduled(cron = "* * 9 * * *")
    public void startNoticeByConcert() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Favorites> favoritesList = favoritesService.getConcertFavoretisList(tomorrow);

        for(Favorites favorites : favoritesList) {

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .receiverId(favorites.getUserId())
                    .noticeType("notice")
                    .message("[콘서트] '" + concertService.getConcertName(favorites.getConcertId()) + "' 가 내일 시작합니다!")
                    .url("/concert/" + favorites.getConcertId())
                    .build();

            producerService.create(noticeDTO);
        }
    }

    @Transactional
    @Scheduled(cron = "* * 9 * * *")
    public void startNoticeByAccompany() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Long> accompanyIdList = accompanyService.getAccompanyListByStartDate(tomorrow);

        List<AccompanyCount> accompanyCounts = accompanyCountService.getAccompanyCountList(accompanyIdList);

        for(AccompanyCount accompanyCount : accompanyCounts) {

            long postId = accompanyService.getPostId(accompanyCount.getAccompanyId());

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .receiverId(accompanyCount.getUserId())
                    .noticeType("notice")
                    .message("[동행] '" + postService.getPostTitle(postId) + "' 동행모임이 내일 출발 예정입니다!")
                    .url("/chat/room/" + chatRoomService.getChatRoomIdByAccompanyId(accompanyCount.getAccompanyId()))
                    .build();

            producerService.create(noticeDTO);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteNoticeIsRead() {

        noticeService.deleteIsRead();
    }
}
