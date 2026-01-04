package com.concertfinder.concertfinder.concert.DTO.infoDTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoImageDTO {

    // 공연 정보 이미지
    @XmlElement(name = "styurl")
    private String imagePath;
}
