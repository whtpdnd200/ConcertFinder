package com.concertfinder.concertfinder.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`concert`")
public class Concert {

    @Id
    private String concertId;

    private String concertName;

    private String posterPath;

    private LocalDate concertDate;

    private String areaCode;

    private String areaName;

    private String state;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
