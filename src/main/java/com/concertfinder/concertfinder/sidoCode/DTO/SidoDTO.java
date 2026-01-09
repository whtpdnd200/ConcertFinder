package com.concertfinder.concertfinder.sidoCode.DTO;

import lombok.*;

@Getter
@Builder(toBuilder = true)
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SidoDTO {

    private String sidoCode;
    private String sidoName;
}
