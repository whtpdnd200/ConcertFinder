package com.concertfinder.concertfinder.concert.DTO.areaDTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AreaInfoDTO {

    // 위도
    @XmlElement(name = "la")
    private String latitude;

    // 경도
    @XmlElement(name = "lo")
    private String longitude;

    // 공연장 주소
    @XmlElement(name = "adres")
    private String address;
}
