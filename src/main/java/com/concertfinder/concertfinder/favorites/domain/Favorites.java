package com.concertfinder.concertfinder.favorites.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@IdClass(FavoritesId.class)
@Entity
@Table(name = "`favorites`")
public class Favorites {

    @Id
    private long userId;

    @Id
    private String concertId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
