package com.concertfinder.concertfinder.concert.DTO.infoDTO;

import com.concertfinder.concertfinder.concert.DTO.areaDTO.AreaInfoDTO;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoDTO {

    // 콘서트 아이디
    @XmlElement(name = "mt20id")
    private String concertId;

    // 콘서트 이름
    @XmlElement(name = "prfnm")
    private String concertName;

    // 시작 날짜
    @XmlElement(name = "prfpdfrom")
    private String sDate;

    // 종료 날짜
    @XmlElement(name = "prfpdto")
    private String eDate;

    // 공연장 이름
    @XmlElement(name = "fcltynm")
    private String areaName;

    // 관람 연령
    @XmlElement(name = "prfage")
    private String ageRatings;

    // 티켓 가격
    @XmlElement(name = "pcseguidance")
    private String ticketPrice;

    // 포스터 이미지 경로
    @XmlElement(name = "poster")
    private String posterPath;

    // 공연 상태
    @XmlElement(name = "prfstate")
    private String state;

    // 공연장 코드
    @XmlElement(name = "mt10id")
    private String areaCode;

    // 공연 시작 시간
    @XmlElement(name = "dtguidance")
    private String sTime;

    // 공연 런타임
    @XmlElement(name = "prfruntime")
    private String runTime;

    // 공연 장르
    @XmlElement(name = "genrenm")
    private String genre;

    // 소개 정보 이미지
    @XmlElement(name = "styurls")
    private List<InfoImageDTO> infoImages;

    // 예매처 이름 및 링크
    @XmlElement(name = "relates")
    private TicketListDTO tickets;

    private AreaInfoDTO areaInfo;

    private boolean isFavorites;

    private int reviewCount;

    private Double averageReview;
}
