package com.concertfinder.concertfinder.notice.service;

import com.concertfinder.concertfinder.kafka.DTO.NoticeDTO;
import com.concertfinder.concertfinder.notice.DTO.NoticeSendDTO;
import com.concertfinder.concertfinder.notice.domain.Notice;
import com.concertfinder.concertfinder.notice.repository.NoticeRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@lombok.extern.slf4j.Slf4j
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    private final NoticeRepository noticeRepository;

    // 구독 기능
    public SseEmitter subscribe(Long userId) {

        long timeout = 1000L * 60 * 60; // sse emitter 연결 시간, 1시간

        // sseEmitter 저장
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitterMap.put(userId, sseEmitter);


        // sseEmitter complete 처리
        sseEmitter.onCompletion(() -> sseEmitterMap.remove(userId));

        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmitterMap.remove(userId);
        });

        sseEmitter.onError((e) -> {
            sseEmitter.complete();
            sseEmitterMap.remove(userId);
        });

        // connect event로 message 발생
        sendToClient(userId, "connect", "sse connect");

        List<NoticeSendDTO> noticeSendList = getNoticeList(userId);

        if(!noticeSendList.isEmpty()) {

            for(NoticeSendDTO noticeSendDTO : noticeSendList) {

                sendToClient(userId, noticeSendDTO.getNoticeType(), noticeSendDTO);
            }
        }



        return sseEmitter;
    }

    // 클라이언트에 메시지 전송
    public void sendToClient(Long userId, String eventName, Object data) {

        String eventId = userId + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = sseEmitterMap.get(userId);

        if(sseEmitter != null) {

            try {

                sseEmitter.send(SseEmitter
                        .event()
                        .id(eventId)
                        .name(eventName)
                        .data(data));

            } catch (IOException e) {

                sseEmitterMap.remove(userId);
                sseEmitter.completeWithError(e);
            }
        }
    }

    // 알림 저장
    public Long insertNotice(NoticeDTO noticeDTO) {

        Notice notice = Notice.builder()
                .receiverId(noticeDTO.getReceiverId())
                .noticeType(noticeDTO.getNoticeType())
                .message(noticeDTO.getMessage())
                .url(noticeDTO.getUrl())
                .isRead(false)
                .build();

        try {

            return noticeRepository.save(notice).getId();

        } catch(DataAccessException e) {

            log.warn("알림 저장 실패 {} ", e.getMessage());
        }

        return null;
    }

    // 알림 목록 반환
    public List<NoticeSendDTO> getNoticeList(long receiverId) {

        return noticeRepository.findAllByReceiverIdAndIsReadFalseOrderByIdDesc(receiverId).stream()
                .map(entity -> NoticeSendDTO.builder()
                        .id(entity.getId())
                        .receiverId(entity.getReceiverId())
                        .noticeType(entity.getNoticeType())
                        .message(entity.getMessage())
                        .url(entity.getUrl())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .toList();
    }

    // 알림 상태 수정
    public void updateIsRead(long id) {

        Optional<Notice> optionalNotice = noticeRepository.findById(id);

        if(!optionalNotice.isPresent()) {

            throw new NoSuchElementException("알림 없음");
        }

        Notice notice = optionalNotice.get();

        notice = notice.toBuilder()
                .isRead(true)
                .build();

        try  {
            noticeRepository.save(notice);
        } catch(DataAccessException e) {

            throw new RuntimeException("읽음 처리 실패!");
        }
    }

    // 알림 전체 읽음 처리
    public void updateReadAll(long userId) {

        noticeRepository.updatedAllByUserId(userId);
    }

    @Transactional
    public void deleteIsRead() {

        noticeRepository.deleteByIsReadTrue();
    }
}
