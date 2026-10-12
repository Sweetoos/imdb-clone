package org.example.imdbclone.security;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.review.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("ownership")
@RequiredArgsConstructor
public class ResourceOwnership {
    private final ReviewRepository reviews;
    private final org.example.imdbclone.user.UserRepository users;
    public boolean username(Authentication authentication, String username) {
        return users.findByUsername(username).map(u -> user(authentication, u.getUserId())).orElse(false);
    }
    public boolean user(Authentication authentication, Long userId) {
        return authentication != null && userId != null && userId.toString().equals(authentication.getName());
    }
    public boolean review(Authentication authentication, Long reviewId) {
        return reviews.findById(reviewId)
                .map(review -> user(authentication, review.getUser().getUserId())).orElse(false);
    }
}
