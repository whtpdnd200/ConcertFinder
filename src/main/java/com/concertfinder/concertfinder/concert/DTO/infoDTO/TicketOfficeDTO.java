package com.concertfinder.concertfinder.concert.DTO.infoDTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketOfficeDTO {

    // 예매처 이름
    @XmlElement(name = "relatenm")
    private String officeName;

    // 예매처 링크
    @XmlElement(name = "relateurl")
    private String officeURL;
}
