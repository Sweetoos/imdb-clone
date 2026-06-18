package org.example.imdbclone.title.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.imdbclone.model.Genre;
import org.example.imdbclone.model.Keyword;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "title")
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
    private Long titleId;

    @Column(name = "title_type")
    @Enumerated(EnumType.STRING)
    private TitleType titleType;

    @Column(name = "title_name")
    private String titleName;

    @Column(name = "explicit_content")
    private boolean explicitContent;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "runtime_minutes")
    private Integer runtimeMinutes;

    @ManyToMany
    @JoinTable(
            name = "title_genre",
            joinColumns = @JoinColumn(name = "title_id", foreignKey = @ForeignKey(name = "fk_title")),
            inverseJoinColumns = @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "fk_genre"))
    )
    @Builder.Default
    private List<Genre> genres=new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "title_keyword",
            joinColumns = @JoinColumn(name = "title_id", foreignKey = @ForeignKey(name = "fk_title_kw")),
            inverseJoinColumns = @JoinColumn(name = "keyword_id", foreignKey = @ForeignKey(name = "fk_keyword_kw"))
    )
    @Builder.Default
    private List<Keyword> keywords=new ArrayList<>();

    //title has persons such as actors, directors etc
}
