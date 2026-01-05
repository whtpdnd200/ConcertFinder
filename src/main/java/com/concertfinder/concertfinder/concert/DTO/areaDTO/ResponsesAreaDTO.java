package com.concertfinder.concertfinder.concert.DTO.areaDTO;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "dbs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsesAreaDTO {

    @XmlElement(name = "db")
    private AreaInfoDTO areaInfoDTO;
}
