package org.example.imdbclone.rating;

import org.example.imdbclone.rating.domain.TitleRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRatingRepository extends JpaRepository<TitleRating, Long> {
}
