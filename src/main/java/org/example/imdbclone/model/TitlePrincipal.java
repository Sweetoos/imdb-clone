package org.example.imdbclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.imdbclone.title.domain.Title;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "title_principals")
@Getter
@Setter
@NoArgsConstructor
public class TitlePrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Title title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person person;

    @Column(nullable = false, length = 100)
    private String category;

    private String job;

    @Column(columnDefinition = "text")
    private String characters;
}