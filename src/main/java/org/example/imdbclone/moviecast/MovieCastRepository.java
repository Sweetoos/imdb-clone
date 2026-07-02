package org.example.imdbclone.moviecast;

import org.example.imdbclone.moviecast.domain.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {
}
