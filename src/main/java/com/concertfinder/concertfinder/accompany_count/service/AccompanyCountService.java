package com.concertfinder.concertfinder.accompany_count.service;

import com.concertfinder.concertfinder.accompany_count.domain.AccompanyCount;
import com.concertfinder.concertfinder.accompany_count.repository.AccompanyCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccompanyCountService {

    private final AccompanyCountRepository accompanyCountRepository;

    // 동행 모집 인원 추가 메서드
    public void insertAccompanyCount(long accompanyId, long userId) {

        AccompanyCount accompanyCount = AccompanyCount.builder()
                .accompanyId(accompanyId)
                .userId(userId)
                .build();

        try {
            accompanyCountRepository.save(accompanyCount);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로인해 동행 정보를 저장 하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }

    // 현재 동행 모집 인원 수 가져오는 메서드
    public int getAccompanyCount(long accompanyId) {

        return accompanyCountRepository.countByAccompanyId(accompanyId);
    }

    // 동행 모집인원 전체 컬럼 삭제 메서드
    public void deleteAllAccompanyCount(long accompanyId) {

        List<AccompanyCount> accompanyCounts = accompanyCountRepository.findAllByAccompanyId(accompanyId);

        for(AccompanyCount accompanyCount : accompanyCounts) {

            deleteAccompanyCount(accompanyCount.getAccompanyId(), accompanyCount.getUserId());
        }
    }

    // 동행 모집인원 삭제 메서드
    public void deleteAccompanyCount(long accompanyId, long userId) {

        Optional<AccompanyCount> optionalAccompanyCount = accompanyCountRepository.findByAccompanyIdAndUserId(accompanyId, userId);

        if(!optionalAccompanyCount.isPresent()) {
            throw new NoSuchElementException("동행 인원 정보를 찾을 수 없습니다! 잠시 후 다시 시도해주세요!");
        }

        AccompanyCount accompanyCount = optionalAccompanyCount.get();

        try {
            accompanyCountRepository.delete(accompanyCount);
        } catch(DataAccessException e) {
            accompanyCountRepository.delete(accompanyCount);
            throw new RuntimeException("서버 에러로 인해 동행 인원 정보를 삭제하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }

    public boolean isAccompanyChecked(long accompanyId, long userId) {

        return accompanyCountRepository.existsByAccompanyIdAndUserId(accompanyId, userId);
    }

    public List<Long> getAccompanyIdTop3List(long userId) {

        List<AccompanyCount> accompanyCounts = accompanyCountRepository.findAllTop3ByUserId(userId);

        List<Long> accompanyIdList = new ArrayList<>();

        for(AccompanyCount a : accompanyCounts) {

            accompanyIdList.add(a.getAccompanyId());
        }

        return accompanyIdList;
    }

    public List<Long> getAccompanyAllIdList(long userId) {

        List<AccompanyCount> accompanyAllCounts = accompanyCountRepository.findAllByUserId(userId);

        List<Long> accompanyAllIdList = new ArrayList<>();

        for(AccompanyCount a : accompanyAllCounts) {

            accompanyAllIdList.add(a.getAccompanyId());
        }

        return accompanyAllIdList;
    }

    public List<AccompanyCount> getAccompanyCountList(List<Long> accompanyIdList) {

        return accompanyCountRepository.findAllByAccompanyIdIn(accompanyIdList);
    }
}
