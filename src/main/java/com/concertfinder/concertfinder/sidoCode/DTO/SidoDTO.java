package com.concertfinder.concertfinder.sidoCode.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SidoDTO implements Serializable {

    private String sidoCode;
    private String sidoName;
}
