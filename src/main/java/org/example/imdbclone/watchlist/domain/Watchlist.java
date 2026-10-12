package org.example.imdbclone.watchlist.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist {

    @EmbeddedId
    private WatchlistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("titleId")
    @JoinColumn(name = "title_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Title title;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;
}