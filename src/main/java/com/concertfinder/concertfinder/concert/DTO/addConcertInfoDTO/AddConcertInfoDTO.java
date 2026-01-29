package com.concertfinder.concertfinder.concert.DTO.addConcertInfoDTO;

import com.concertfinder.concertfinder.common.LocalDateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;

import java.time.LocalDate;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AddConcertInfoDTO {

    @XmlElement(name = "mt20id")
    private String concertId;

    @XmlElement(name = "prfnm")
    private String concertName;

    @XmlElement(name = "poster")
    private String posterPath;

    @XmlElement(name = "prfpdfrom")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate concertDate;

    @XmlElement(name = "mt10id")
    private String areaCode;

    @XmlElement(name = "fcltynm")
    private String areaName;

    @XmlElement(name = "prfstate")
    private String state;
}
