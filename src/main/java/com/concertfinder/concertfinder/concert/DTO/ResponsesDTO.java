package com.concertfinder.concertfinder.concert.DTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "dbs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsesDTO {
    
}
