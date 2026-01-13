package com.concertfinder.concertfinder.ladder.service;

import com.concertfinder.concertfinder.accompany.DTO.AccompanyAddDTO;
import com.concertfinder.concertfinder.accompany.DTO.AccompanyInfoDTO;
import com.concertfinder.concertfinder.accompany.domain.Accompany;
import com.concertfinder.concertfinder.accompany.service.AccompanyService;
import com.concertfinder.concertfinder.accompany_count.service.AccompanyCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccompanyAndAccompanyCountLadderService {

    private final AccompanyService accompanyService;

    private final AccompanyCountService accompanyCountService;

    @Transactional
    public void insertAccompanyAndAccompanyCount(AccompanyAddDTO accompanyAddDTO) {

        long accompanyId = accompanyService.insertAccompany(accompanyAddDTO);

        accompanyCountService.insertAccompanyCount(accompanyId, accompanyAddDTO.getUserId());
    }

    @Transactional
    public void deleteAccompanyAndAccompanyCount(long postId) {

        long accompanyId = accompanyService.getAccompanyId(accompanyService.getAccompanyId(postId));
        accompanyService.deleteAccompany(accompanyId);
        accompanyCountService.deleteAllAccompanyCount(accompanyId);
    }

    @Transactional
    public void insertAccompanyCountAndIsFullCheck(long accompanyId, long userId) {
        Accompany accompany = accompanyService.getAccompany(accompanyId);

        accompanyCountService.insertAccompanyCount(accompanyId, userId);

        if(accompanyService.isFull(accompany)) {

            accompanyService.isFullChange(accompany, true);
        } else {
            accompanyService.isFullChange(accompany, false);
        }
    }
    

    public AccompanyInfoDTO getAccompanyInfo(long postId) {

        return accompanyService.getAccompanyInfo(postId);
    }
}
