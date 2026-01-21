package com.concertfinder.concertfinder.chat_room;

import com.concertfinder.concertfinder.chat_room.DTO.ChatRoomListDTO;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
import com.concertfinder.concertfinder.ladder.service.ChatRoomAndChatRoomAndUserLadderService;
import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomRestController {

    private final AccompanyAndAccompanyCountLadderService accompanyAndAccompanyCountLadderService;
    private final ChatRoomAndChatRoomAndUserLadderService chatRoomAndChatRoomAndUserLadderService;
    private final PostService postService;

    // 채팅방 목록 출력 API
    @GetMapping("/{userId}/list")
    public ResponseEntity<ApiResponseDTO<List<ChatRoomListDTO>>> getChatRoomList(@PathVariable long userId) {

        List<ChatRoomListDTO> chatRoomList = accompanyAndAccompanyCountLadderService.getChatRoomList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.success("목록 출력 성공", chatRoomList));
    }

    // 채팅방 퇴장 및 동행 취소 API
    @DeleteMapping("/{accompanyId}")
    public ResponseEntity<ApiResponseDTO<Void>> exitChatRoom(@PathVariable long accompanyId
                                                            , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        accompanyAndAccompanyCountLadderService.deleteAccompanyCountAndIsFullCheck(accompanyId, loginUserDTO.getId(), "exit");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("퇴장 성공"));
    }

    // 채팅방 삭제 및 동행 모집 게시글 삭제
    @DeleteMapping("/{accompanyId}/{roomId}")
    public ResponseEntity<ApiResponseDTO<Void>> exitChatRoom(@PathVariable long accompanyId
                                                             , @PathVariable Long roomId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        postService.postDeleteByAccompanyId(accompanyId, loginUserDTO.getId(), roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("삭제 성공"));
    }

    // 1:1 채팅방 생성 및 이동
    @PostMapping("/{otherUserId}")
    public ResponseEntity<ApiResponseDTO<Long>> oneToOneChat(@PathVariable long otherUserId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {
        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        Long roomId = chatRoomAndChatRoomAndUserLadderService.createPrivateChatRoom(loginUserDTO.getId(), otherUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("생성 성공", roomId));
    }

    // 1:1 채팅방 삭제 및 유저 목록 삭제
    @DeleteMapping("/private/{roomId}")
    public ResponseEntity<ApiResponseDTO<Void>> deletePrivateChatRoom(@PathVariable long roomId) {

        chatRoomAndChatRoomAndUserLadderService.deletePrivateChatRoomAndUser(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("삭제 완료"));
    }
}
