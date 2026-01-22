package com.concertfinder.concertfinder.last_chat.domain;

import com.concertfinder.concertfinder.favorites.domain.FavoritesId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@IdClass(LastChatId.class)
@Entity
@Table(name = "`last_chat`")
public class LastChat {

    @Id
    private long roomId;

    @Id
    private long userId;

    private long chatId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public LastChat(long roomId, long userId) {

        this.roomId = roomId;
        this.userId = userId;
    }
}
