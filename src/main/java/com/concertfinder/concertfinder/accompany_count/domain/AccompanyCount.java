package com.concertfinder.concertfinder.accompany_count.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder()
@IdClass(AccompanyCountId.class)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`accompany_count`")
public class AccompanyCount {

    @Id
    private long accompanyId;

    @Id
    private long userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
