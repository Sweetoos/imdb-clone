package org.example.imdbclone.moviecast;

import org.example.imdbclone.moviecast.domain.JobRole;
import org.example.imdbclone.moviecast.domain.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long> {
    List<MovieCast> findByTitleTitleId(Long titleId);

    List<MovieCast> findByPersonPersonId(Long personId);

    boolean existsByTitleTitleIdAndPersonPersonIdAndJobRole(Long titleId, Long personId, JobRole jobRole);
}
