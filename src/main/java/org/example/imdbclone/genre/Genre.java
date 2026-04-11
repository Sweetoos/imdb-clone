package org.example.imdbclone.genre;

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
    private Integer genreId;

    @Column(name = "genre_name")
    private String genreName;

    public Genre(String genreName){
        this.genreName=genreName;
    }
}
