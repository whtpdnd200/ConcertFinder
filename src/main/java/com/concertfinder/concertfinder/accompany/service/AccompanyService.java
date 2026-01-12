package com.concertfinder.concertfinder.accompany.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.repository.AccompanyRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccompanyService {

    private final AccompanyRepository accompanyRepository;

    public void insertAccompany(AccompanyAddDTO accompanyAddDTO
                                , long postId
                                , long userId) {

        Accompany accompany = Accompany.builder()
                .postId(postId)
                .userId(userId)
                .headCount(accompanyAddDTO.getHeadCount())
                .place(accompanyAddDTO.getPlace())
                .sDateTime(accompanyAddDTO.getSDateTime())
                .isFull(false)
                .build();

        try {
            accompanyRepository.save(accompany);

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로 인해 동행 정보를 저장 하지 못했습니다 잠시 후 다시 시도 해주세요!");
        }
    }
}
