package com.concertfinder.concertfinder.SidoCode.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SidoDTO {

    private String sidoCode;
    private String sidoName;
}
