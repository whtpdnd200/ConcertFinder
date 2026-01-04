package com.concertfinder.concertfinder.concert.DTO.infoDTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "dbs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsesInfoDTO {

    @XmlElement(name = "db")
    private InfoDTO infoDTO;
}
