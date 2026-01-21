package com.concertfinder.concertfinder.chat_room_and_user.domain;

import com.concertfinder.concertfinder.favorites.domain.FavoritesId;
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
@IdClass(ChatRoomAndUserId.class)
@Entity
@Table(name = "`chat_room_and_user`")
public class ChatRoomAndUser {

    @Id
    private long userId;

    @Id
    private long roomId;

    private boolean isHost;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
