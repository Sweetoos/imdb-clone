package org.example.imdbclone.genre;

import org.example.imdbclone.genre.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreNameIgnoreCase(String genreName);
    boolean existsByGenreNameIgnoreCase(String genreName);
}
