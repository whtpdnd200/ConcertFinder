package com.concertfinder.concertfinder.SidoCode.service;

import com.concertfinder.concertfinder.SidoCode.DTO.SidoDTO;
import com.concertfinder.concertfinder.SidoCode.domain.SidoCode;
import com.concertfinder.concertfinder.SidoCode.repository.SidoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SidoCodeService {

    private final SidoCodeRepository sidoCodeRepository;

    public List<SidoDTO> getAllCode() {

        List<SidoDTO> sidoDTOList = new ArrayList<>();

        List<SidoCode> sidoCodes = sidoCodeRepository.findAll();

        for(SidoCode sidoCode : sidoCodes) {

            SidoDTO sidoDTO = SidoDTO.builder()
                    .sidoCode(sidoCode.getSidoCode())
                    .sidoName(sidoCode.getSidoName())
                    .build();

            sidoDTOList.add(sidoDTO);
        }

        return sidoDTOList;
    }
}
