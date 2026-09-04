package org.example.imdbclone.watchlist.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WatchlistId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title_id")
    private Long titleId;
}