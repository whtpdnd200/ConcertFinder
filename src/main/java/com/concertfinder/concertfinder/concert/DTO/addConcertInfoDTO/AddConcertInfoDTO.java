package com.concertfinder.concertfinder.concert.DTO.addConcertInfoDTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AddConcertInfoDTO {

    @XmlElement(name = "mt20id")
    private String concertId;

    @XmlElement(name = "prfnm")
    private String concertName;

    @XmlElement(name = "poster")
    private String posterPath;

    @XmlElement(name = "mt10id")
    private String areaCode;

    @XmlElement(name = "fcltynm")
    private String areaName;

    @XmlElement(name = "prfstate")
    private String state;
}
