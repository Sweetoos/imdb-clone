package org.example.imdbclone.moviecast.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.title.domain.Title;

@Getter
@Setter
@Table(name = "movie_cast")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MovieCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id")
    private Title title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "character_name")
    private String characterName;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_role")
    private JobRole jobRole;

}
