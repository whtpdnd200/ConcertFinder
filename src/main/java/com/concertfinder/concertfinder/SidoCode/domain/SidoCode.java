package com.concertfinder.concertfinder.SidoCode.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "`sido_code`")
public class SidoCode {

    @Id
    private byte sidoCode;

    private String sidoName;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
