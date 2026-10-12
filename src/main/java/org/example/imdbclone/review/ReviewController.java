package org.example.imdbclone.review;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.review.dto.ReviewCreateDto;
import org.example.imdbclone.review.dto.ReviewPatchDto;
import org.example.imdbclone.review.dto.ReviewResponseDto;
import org.example.imdbclone.review.dto.ReviewUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@ownership.user(authentication, #p0.userId())")
    public ReviewResponseDto createReview(@RequestBody @Valid ReviewCreateDto dto) {
        return reviewService.createReview(dto);
    }

    @GetMapping("/title/{titleId}")
    public List<ReviewResponseDto> getReviewsForTitle(@PathVariable Long titleId) {
        return reviewService.getReviewsForTitle(titleId);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponseDto> getReviewsForUser(@PathVariable Long userId) {
        return reviewService.getReviewsForUser(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ownership.review(authentication, #p0)")
    public ReviewResponseDto updateReview(@PathVariable Long id, @RequestBody @Valid ReviewUpdateDto dto) {
        return reviewService.updateReview(id, dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@ownership.review(authentication, #p0)")
    public ReviewResponseDto patchReview(@PathVariable Long id, @RequestBody @Valid ReviewPatchDto dto) {
        return reviewService.patchReview(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@ownership.review(authentication, #p0)")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
