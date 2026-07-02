package org.example.imdbclone.moviecast.domain;

import jakarta.persistence.*;
import org.example.imdbclone.person.domain.Person;
import org.example.imdbclone.title.domain.Title;

@Entity
public class MovieCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private String characterName;

    @Enumerated(EnumType.STRING)
    private JobRole jobRole;

}
