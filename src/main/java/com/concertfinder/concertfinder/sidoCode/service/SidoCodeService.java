package com.concertfinder.concertfinder.sidoCode.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.concertfinder.concertfinder.sidoCode.DTO.SidoDTO;
import com.concertfinder.concertfinder.sidoCode.domain.SidoCode;
import com.concertfinder.concertfinder.sidoCode.repository.SidoCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Slf4j
@RequiredArgsConstructor
@Service
public class SidoCodeService {

    private final SidoCodeRepository sidoCodeRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String SIDO_PREFIX = "sido:list";

    // 시 도 목록 반환 메서드
    public List<SidoDTO> getAllCode() {

        Object cachedList = redisTemplate.opsForValue().get(SIDO_PREFIX);

        if (cachedList != null) {

            log.info("Sido List Cache Hit {} ", cachedList);
            return objectMapper.convertValue(cachedList, new TypeReference<List<SidoDTO>>() {});
        }

        List<SidoDTO> sidoDTOList = new ArrayList<>();

        List<SidoCode> sidoCodes = sidoCodeRepository.findAll();

        log.info("Sido List Cache Miss");

        for(SidoCode sidoCode : sidoCodes) {

            SidoDTO sidoDTO = SidoDTO.builder()
                    .sidoCode(sidoCode.getSidoCode())
                    .sidoName(sidoCode.getSidoName())
                    .build();

            sidoDTOList.add(sidoDTO);
        }

        if(sidoCodes != null || !sidoCodes.isEmpty()) {

            redisTemplate.opsForValue().set(SIDO_PREFIX, sidoDTOList, java.time.Duration.ofDays(1));
        }

        return sidoDTOList;
    }

    // 시 도 이름 반환 메서드
    public String getSidoName(String sidoCode) {

        if(sidoCode == null || sidoCode.equals("") ||  sidoCode.equals("00")) {
            return "모든 지역";
        }

        Object cacheSidoLit = redisTemplate.opsForValue().get(SIDO_PREFIX);
        
        if(cacheSidoLit != null) {
            
            List<SidoDTO> sidoList = objectMapper.convertValue(cacheSidoLit, new TypeReference<List<SidoDTO>>() {});

            log.info("sido name cache Hit");

            return sidoList.stream()
                    // 전송된 시도 코드와 저장된 시도코드가 일치하는 값을 찾음
                    .filter(sido -> sido.getSidoCode().equals(sidoCode))
                    // 일치하는 값의 이름만 추출추출
                    .map(SidoDTO::getSidoName)
                    // 반복문의 break 처럼 값을 찾으면 바로 반복 종료
                    .findFirst()
                    .orElse("알 수 없는 지역");
        }

        return sidoCodeRepository.findBySidoCode(sidoCode).getSidoName();
    }
}
