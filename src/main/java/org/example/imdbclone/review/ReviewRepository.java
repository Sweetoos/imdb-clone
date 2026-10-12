package org.example.imdbclone.review;

import org.example.imdbclone.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTitleTitleId(Long titleId);
    List<Review> findByUserUserId(Long personId);
    boolean existsByUserUserIdAndTitleTitleId(Long userId, Long titleId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.title.titleId=:titleId")
    Double calculateAverageRatingByTitleId(@Param("titleId") Long titleId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.title.titleId=:titleId")
    Integer countReviewsByTitleId(@Param("titleId") Long titleId);
}
