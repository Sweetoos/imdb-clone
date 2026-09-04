package org.example.imdbclone.review;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TitleRepository titleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TitleRatingRepository titleRatingRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Title sampleTitle;
    private User sampleUser;
    private Review sampleReview;

    @BeforeEach
    void setUp() {
        sampleTitle = Title.builder()
                .titleId(1L)
                .titleName("Inception")
                .build();

        sampleUser = User.builder()
                .userId(10L)
                .username("john_doe")
                .build();

        sampleReview = Review.builder()
                .reviewId(100L)
                .title(sampleTitle)
                .user(sampleUser)
                .rating(9)
                .reviewText("Amazing movie!")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("[REVIEW] SERVICE: CREATE")
    class CreateReviewTests {

        @Test
        @DisplayName("Should create review and update title rating stats")
        void shouldCreateReviewSuccessfully() {
            ReviewCreateDto dto = new ReviewCreateDto(1L, 10L, 9, "Amazing movie!");

            when(titleRepository.findById(1L)).thenReturn(Optional.of(sampleTitle));
            when(userRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
            when(reviewRepository.existsByUserUserIdAndTitleTitleId(10L, 1L)).thenReturn(false);
            when(reviewRepository.save(any(Review.class))).thenReturn(sampleReview);

            when(reviewRepository.calculateAverageRatingByTitleId(1L)).thenReturn(9.0);
            when(reviewRepository.countReviewsByTitleId(1L)).thenReturn(1);
            when(titleRatingRepository.findById(1L)).thenReturn(Optional.empty());

            ReviewResponseDto response = reviewService.createReview(dto);

            assertThat(response).isNotNull();
            assertThat(response.rating()).isEqualTo(9);
            assertThat(response.titleName()).isEqualTo("Inception");
            assertThat(response.username()).isEqualTo("john_doe");

            verify(reviewRepository, times(1)).save(any(Review.class));
            verify(titleRatingRepository, times(1)).save(any(TitleRating.class));
        }

        @Test
        @DisplayName("Should throw DuplicateReviewException when user already reviewed title")
        void shouldThrowExceptionWhenReviewAlreadyExists() {
            ReviewCreateDto dto = new ReviewCreateDto(1L, 10L, 9, "Amazing movie!");

            when(titleRepository.findById(1L)).thenReturn(Optional.of(sampleTitle));
            when(userRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
            when(reviewRepository.existsByUserUserIdAndTitleTitleId(10L, 1L)).thenReturn(true);

            assertThatThrownBy(() -> reviewService.createReview(dto))
                    .isInstanceOf(DuplicateReviewException.class)
                    .hasMessageContaining("User has already reviewed this title");

            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("[REVIEW] SERVICE: READ")
    class ReadReviewTests {

        @Test
        @DisplayName("Should return reviews for given title ID")
        void shouldReturnReviewsForTitle() {
            when(titleRepository.existsById(1L)).thenReturn(true);
            when(reviewRepository.findByTitleTitleId(1L)).thenReturn(List.of(sampleReview));

            List<ReviewResponseDto> result = reviewService.getReviewsForTitle(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).reviewText()).isEqualTo("Amazing movie!");
        }
    }

    @Nested
    @DisplayName("[REVIEW] SERVICE: UPDATE")
    class UpdateReviewTests {

        @Test
        @DisplayName("Should update review and recalculate title rating")
        void shouldUpdateReviewSuccessfully() {
            ReviewUpdateDto updateDto = new ReviewUpdateDto(10, "Masterpiece!");

            when(reviewRepository.findById(100L)).thenReturn(Optional.of(sampleReview));
            when(reviewRepository.calculateAverageRatingByTitleId(1L)).thenReturn(10.0);
            when(reviewRepository.countReviewsByTitleId(1L)).thenReturn(1);
            when(titleRatingRepository.findById(1L)).thenReturn(Optional.empty());

            ReviewResponseDto result = reviewService.updateReview(100L, updateDto);

            assertThat(result.rating()).isEqualTo(10);
            assertThat(result.reviewText()).isEqualTo("Masterpiece!");
            verify(titleRatingRepository, times(1)).save(any(TitleRating.class));
        }
    }

    @Nested
    @DisplayName("[REVIEW] SERVICE: PATCH")
    class PatchReviewTests {

        @Test
        @DisplayName("Should patch only review text without recalculating title rating")
        void shouldPatchOnlyReviewText() {
            Long reviewId = 100L;
            ReviewPatchDto patchDto = new ReviewPatchDto(null, "Updated text only!");

            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(sampleReview));

            ReviewResponseDto result = reviewService.patchReview(reviewId, patchDto);

            assertThat(result).isNotNull();
            assertThat(result.reviewText()).isEqualTo("Updated text only!");
            assertThat(result.rating()).isEqualTo(9);

            verify(titleRatingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should patch rating and recalculate title rating stats")
        void shouldPatchRatingAndRecalculateStats() {
            Long reviewId = 100L;
            ReviewPatchDto patchDto = new ReviewPatchDto(10, null);

            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(sampleReview));
            when(reviewRepository.calculateAverageRatingByTitleId(sampleTitle.getTitleId())).thenReturn(10.0);
            when(reviewRepository.countReviewsByTitleId(sampleTitle.getTitleId())).thenReturn(1);
            when(titleRatingRepository.findById(sampleTitle.getTitleId())).thenReturn(Optional.empty());

            ReviewResponseDto result = reviewService.patchReview(reviewId, patchDto);

            assertThat(result).isNotNull();
            assertThat(result.rating()).isEqualTo(10);
            assertThat(result.reviewText()).isEqualTo("Amazing movie!");

            verify(titleRatingRepository, times(1)).save(any(TitleRating.class));
        }

        @Test
        @DisplayName("Should throw ReviewNotFoundException when patching non-existing review")
        void shouldThrowExceptionWhenReviewNotFound() {
            Long nonExistingId = 11111111L;
            ReviewPatchDto patchDto = new ReviewPatchDto(8, "Text");

            when(reviewRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> reviewService.patchReview(nonExistingId, patchDto))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));
        }
    }

    @Nested
    @DisplayName("[REVIEW] SERVICE: DELETE")
    class DeleteReviewTests {

        @Test
        @DisplayName("Should delete review and recalculate title rating")
        void shouldDeleteReviewSuccessfully() {
            when(reviewRepository.findById(100L)).thenReturn(Optional.of(sampleReview));
            when(reviewRepository.calculateAverageRatingByTitleId(1L)).thenReturn(0.0);
            when(reviewRepository.countReviewsByTitleId(1L)).thenReturn(0);
            when(titleRatingRepository.findById(1L)).thenReturn(Optional.empty());

            reviewService.deleteReview(100L);

            verify(reviewRepository, times(1)).delete(sampleReview);
            verify(titleRatingRepository, times(1)).save(any(TitleRating.class));
        }
    }
}