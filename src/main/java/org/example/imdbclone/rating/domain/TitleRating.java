package org.example.imdbclone.rating.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.imdbclone.title.domain.Title;

@Entity
@Table(name = "title_rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitleRating {
    @Id
    @Column(name = "title_id")
    private Long titleId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "average_rating", nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(name = "num_votes", nullable = false)
    @Builder.Default
    private Integer numVotes = 0;
}
