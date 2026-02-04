package com.concertfinder.concertfinder.notice;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.notice.service.NoticeService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class NoticeRestController {

    private final NoticeService noticeService;

    // SSE 연결
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal PrincipalDetails principal) {

        LoginUserDTO loginUserDTO = principal.getLoginUserDTO();

        return noticeService.subscribe(loginUserDTO.getId());
    }

//    @PostMapping("/send")
//    public void sendNotice(@RequestBody NoticeDTO noticeDTO) {
//
//
//        noticeService.sendToClient(noticeDTO.getReceiverId(), noticeDTO.getNoticeType(), noticeDTO);
//    }

    // 단일 알림 읽음 처리
    @PatchMapping("/modify/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> isReadModify(@PathVariable long id) {

        noticeService.updateIsRead(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success(null));
    }

    // 모든 알림 읽음 처리
    @PatchMapping("/modify-all")
    public ResponseEntity<ApiResponseDTO<Void>> readAllNotice(@AuthenticationPrincipal PrincipalDetails principalDetails) {


        LoginUserDTO loginUserDTO = principalDetails.getLoginUserDTO();

        noticeService.updateReadAll(loginUserDTO.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDTO.success(null));
    }
}
