package org.example.imdbclone.security;

import org.example.imdbclone.user.*;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.dto.UserResponseDto;
import org.example.imdbclone.review.*;
import org.example.imdbclone.review.domain.Review;
import org.example.imdbclone.watchlist.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, UserController.class, ReviewController.class, WatchlistController.class})
@Import({SecurityConfigMain.class, JwtService.class, ResourceOwnership.class})
class SecurityIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired JwtService jwt;
    @Autowired PasswordEncoder passwords;
    @MockitoBean UserRepository users;
    @MockitoBean UserService userService;
    @MockitoBean ReviewRepository reviews;
    @MockitoBean ReviewService reviewService;
    @MockitoBean WatchlistService watchlistService;

    private String bearer() {
        when(users.existsById(1L)).thenReturn(true);
        return "Bearer " + jwt.generateToken(1L);
    }

    @Test void loginAndMe() throws Exception {
        var user = User.builder().userId(1L).username("alice").passwordHash(passwords.encode("password123")).build();
        when(users.findByUsername("alice")).thenReturn(Optional.of(user));
        when(userService.getUserById(1L)).thenReturn(new UserResponseDto(1L, "alice", "alice@example.com", null));
        mvc.perform(post("/auth/login").contentType("application/json")
                .content("{\"username\":\"alice\",\"password\":\"password123\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.user.username").value("alice"));
        mvc.perform(get("/auth/me").header("Authorization", bearer()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(1));
    }

    @Test void rejectsBadCredentialsAndInvalidInput() throws Exception {
        mvc.perform(post("/auth/login").contentType("application/json")
                .content("{\"username\":\"unknown\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/auth/register").contentType("application/json")
                .content("{\"username\":\"a\",\"email\":\"bad\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest());
        verify(userService, never()).createUser(any());
    }

    @Test void registersAndReturnsToken() throws Exception {
        when(userService.createUser(any())).thenReturn(new UserResponseDto(1L,"alice","alice@example.com",null));
        mvc.perform(post("/auth/register").contentType("application/json")
                .content("{\"username\":\"alice\",\"email\":\"alice@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.token").isString());
    }

    @Test void rejectsMissingTamperedExpiredAndDeletedUserTokens() throws Exception {
        mvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/auth/me").header("Authorization", "Bearer broken")).andExpect(status().isUnauthorized());
        String forged = new JwtService("", 3600000).generateToken(1L);
        mvc.perform(get("/auth/me").header("Authorization", "Bearer " + forged)).andExpect(status().isUnauthorized());
        String secret = java.util.Base64.getEncoder().encodeToString(new byte[32]);
        var expired = new JwtService(secret, 1);
        String token = expired.generateToken(1L);
        Thread.sleep(1100);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> expired.extractUserId(token))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
        mvc.perform(get("/auth/me").header("Authorization", "Bearer " + jwt.generateToken(99L)))
                .andExpect(status().isUnauthorized());
    }

    @Test void protectsAccountsWatchlistsAndReviews() throws Exception {
        String token = bearer();
        mvc.perform(get("/api/users/2").header("Authorization", token)).andExpect(status().isForbidden());
        mvc.perform(get("/api/users").header("Authorization", token)).andExpect(status().isForbidden());
        mvc.perform(get("/api/watchlists/user/2").header("Authorization", token)).andExpect(status().isForbidden());
        mvc.perform(post("/api/reviews").header("Authorization", token).contentType("application/json")
                .content("{\"userId\":2,\"titleId\":1,\"rating\":8}"))
                .andExpect(status().isForbidden());
        when(reviews.findById(10L)).thenReturn(Optional.of(Review.builder()
                .user(User.builder().userId(2L).build()).build()));
        mvc.perform(delete("/api/reviews/10").header("Authorization", token)).andExpect(status().isForbidden());
        mvc.perform(get("/api/watchlists/user/1").header("Authorization", token)).andExpect(status().isOk());
        verify(reviewService, never()).deleteReview(any());
        verify(watchlistService).getUserWatchlist(1L);
    }

    @Test void permitsPublicReviewsAndCorsPreflight() throws Exception {
        mvc.perform(get("/api/reviews/title/1")).andExpect(status().isOk());
        mvc.perform(options("/auth/login").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
}
