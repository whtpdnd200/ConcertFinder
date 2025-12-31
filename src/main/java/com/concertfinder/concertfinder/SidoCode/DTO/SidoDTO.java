package com.concertfinder.concertfinder.SidoCode.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SidoDTO {

    private byte sidoCode;
    private String sidoName;
}
