package org.example.imdbclone.model;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "genre_name")
    private String genreName;

}
