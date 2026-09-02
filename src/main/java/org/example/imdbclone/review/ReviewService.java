package org.example.imdbclone.review;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.rating.TitleRatingRepository;
import org.example.imdbclone.rating.domain.TitleRating;
import org.example.imdbclone.review.domain.Review;
import org.example.imdbclone.review.dto.ReviewCreateDto;
import org.example.imdbclone.review.dto.ReviewPatchDto;
import org.example.imdbclone.review.dto.ReviewResponseDto;
import org.example.imdbclone.review.dto.ReviewUpdateDto;
import org.example.imdbclone.review.exception.DuplicateReviewException;
import org.example.imdbclone.review.exception.ReviewNotFoundException;
import org.example.imdbclone.title.TitleRepository;
import org.example.imdbclone.title.domain.Title;
import org.example.imdbclone.title.exception.TitleNotFoundException;
import org.example.imdbclone.user.UserRepository;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TitleRepository titleRepository;
    private final UserRepository userRepository;
    private final TitleRatingRepository titleRatingRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewCreateDto dto) {
        Title title = titleRepository.findById(dto.titleId())
                .orElseThrow(() -> new RuntimeException("Title not found with id " + dto.titleId()));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + dto.userId()));

        if (reviewRepository.existsByUserUserIdAndTitleTitleId(dto.userId(), dto.titleId())) {
            throw new DuplicateReviewException("User has already reviewed this title");
        }

        Review review = Review.builder()
                .title(title)
                .user(user)
                .rating(dto.rating())
                .reviewText(dto.reviewText())
                .build();

        Review savedReview = reviewRepository.save(review);

        updateTitleRatingStats(title);

        return mapToResponseDto(savedReview);
    }

    public List<ReviewResponseDto> getReviewsForTitle(Long titleId) {
        if (!titleRepository.existsById(titleId)) {
            throw new TitleNotFoundException(titleId);
        }
        return reviewRepository.findByTitleTitleId(titleId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<ReviewResponseDto> getReviewsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return reviewRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewUpdateDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        review.setRating(dto.rating());
        review.setReviewText(dto.reviewText());

        updateTitleRatingStats(review.getTitle());

        return mapToResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto patchReview(Long id, ReviewPatchDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        boolean ratingChanged = false;

        if (dto.rating() != null) {
            review.setRating(dto.rating());
            ratingChanged = true;
        }
        if (dto.reviewText() != null) {
            review.setReviewText(dto.reviewText());
        }

        if (ratingChanged) {
            updateTitleRatingStats(review.getTitle());
        }

        return mapToResponseDto(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
        Title title = review.getTitle();
        reviewRepository.delete(review);
        updateTitleRatingStats(title);
    }

    private ReviewResponseDto mapToResponseDto(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getTitle()
                        .getTitleId(),
                review.getTitle()
                        .getTitleName(),
                review.getUser()
                        .getUserId(),
                review.getUser()
                        .getUsername(),
                review.getRating(),
                review.getReviewText(),
                review.getCreatedAt()
        );
    }

    private void updateTitleRatingStats(Title title) {
        Double avg = reviewRepository.calculateAverageRatingByTitleId(title.getTitleId());
        Integer count = reviewRepository.countReviewsByTitleId(title.getTitleId());

        double roundedAvg = 0.0;
        if (avg != null) {
            roundedAvg = BigDecimal.valueOf(avg)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        TitleRating titleRating = titleRatingRepository.findById(title.getTitleId())
                .orElse(TitleRating.builder()
                        .title(title)
                        .titleId(title.getTitleId())
                        .build());

        titleRating.setAverageRating(roundedAvg);
        titleRating.setNumVotes(count != null ? count : 0);

        titleRatingRepository.save(titleRating);
    }
}
