package com.concertfinder.concertfinder.chat_room;

import com.concertfinder.concertfinder.chat_room.DTO.ChatRoomListDTO;
import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.ladder.service.AccompanyAndAccompanyCountLadderService;
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
        accompanyAndAccompanyCountLadderService.deleteAccompanyCountAndIsFullCheck(accompanyId, loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("퇴장 성공"));
    }

    @DeleteMapping("/{accompanyId}/{roomId}")
    public ResponseEntity<ApiResponseDTO<Void>> exitChatRoom(@PathVariable long accompanyId
                                                             , @PathVariable Long roomId
                                                             , @AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();
        postService.postDeleteByAccompanyId(accompanyId, loginUserDTO.getId(), roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success("삭제 성공"));
    }
}
