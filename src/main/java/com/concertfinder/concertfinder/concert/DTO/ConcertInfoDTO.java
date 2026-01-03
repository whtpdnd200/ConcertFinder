package com.concertfinder.concertfinder.concert.DTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;



@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ConcertInfoDTO {

    @XmlElement(name = "mt20id")
    private String concertId;

    @XmlElement(name = "prfnm")
    private String concertName;

    @XmlElement(name = "poster")
    private String posterPath;

    @XmlElement(name = "area")
    private String area;

    @XmlElement(name = "prfstate")
    private String state;

    @XmlElement(name = "prfpdfrom")
    private String sDate;

    @XmlElement(name = "prfpdto")
    private String eDate;
}
