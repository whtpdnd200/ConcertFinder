package com.concertfinder.concertfinder.accompany.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.repository.AccompanyRepository;
import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccompanyService {

    private final AccompanyRepository accompanyRepository;
    private final AccompanyCountService accompanyCountService;

    @Transactional
    public long insertAccompany(AccompanyAddDTO accompanyAddDTO) {

        if(accompanyAddDTO.getHeadCount() <= 1) {
            throw new IllegalArgumentException("동행 인원은 최소 2명부터 가능 합니다!");
        }

        if(accompanyAddDTO.getSDateTime() == null || accompanyAddDTO.getSDateTime().equals("")) {

            throw new IllegalArgumentException("동행 날짜는 필수로 선택 하셔야 합니다!");
        }

        if(accompanyAddDTO.getPlace() == null || accompanyAddDTO.getPlace().equals("")) {

            throw new IllegalArgumentException("모임 장소는 필수로 입력 하셔야 합니다!");
        }

        Accompany accompany = Accompany.builder()
                .postId(accompanyAddDTO.getPostId())
                .userId(accompanyAddDTO.getUserId())
                .headCount(accompanyAddDTO.getHeadCount())
                .place(accompanyAddDTO.getPlace())
                .sDateTime(accompanyAddDTO.getSDateTime())
                .isFull(false)
                .build();

        try {
            return accompanyRepository.save(accompany).getId();

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로 인해 동행 정보를 저장 하지 못했습니다 잠시 후 다시 시도 해주세요!");
        }
    }

    public long getAccompanyId(long postId) {

        return accompanyRepository.findByPostId(postId).get().getId();
    }

    public AccompanyInfoDTO getAccompanyInfo(long postId) {

        Optional<Accompany> optionalAccompany = accompanyRepository.findByPostId(postId);

        if(!optionalAccompany.isPresent()) {

            throw new NoSuchElementException("동행 정보를 불어오지 못했습니다!");
        }

        Accompany accompany = optionalAccompany.get();

        AccompanyInfoDTO accompanyInfoDTO = AccompanyInfoDTO.builder()
                .id(accompany.getId())
                .userId(accompany.getUserId())
                .currentCount(accompanyCountService.getAccompanyCount(accompany.getId()))
                .headCount(accompany.getHeadCount())
                .place(accompany.getPlace())
                .isFull(accompany.isFull())
                .sDateTime(accompany.getSDateTime())
                .build();

        return accompanyInfoDTO;
    }

    // 동행 정보 삭제 메서드
    public void deleteAccompany(long accompanyId) {

        Optional<Accompany> optionalAccompany = accompanyRepository.findById(accompanyId);

        if(!optionalAccompany.isPresent()) {

            throw new NoSuchElementException("동행 정보를 찾을 수 없습니다!");
        }

        Accompany accompany = optionalAccompany.get();

        try {
            accompanyRepository.delete(accompany);

        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로 인해 동행 정보를 삭제 하지 못했습니다 잠시 후 다시 시도 해주세요!");
        }
    }

    public boolean isFull(Accompany accompany) {


        return accompany.getHeadCount() == accompanyCountService.getAccompanyCount(accompany.getId());
    }

    public Accompany getAccompany(long accompanyId) {
        Optional<Accompany> optionalAccompany = accompanyRepository.findById(accompanyId);

        if(!optionalAccompany.isPresent()) {

            throw new NoSuchElementException("동행 정보를 찾을 수 없습니다! 잠시 후 다시 시도해주세요!");
        }

        return optionalAccompany.get();
    }

    public void isFullChange(Accompany accompany, boolean isFull) {

        accompany = accompany.toBuilder()
                .isFull(isFull)
                .build();

        try {
            accompanyRepository.save(accompany);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 동행 신청 정보를 저장하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }
}
