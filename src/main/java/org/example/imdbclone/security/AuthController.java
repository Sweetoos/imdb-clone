package org.example.imdbclone.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.user.UserRepository;
import org.example.imdbclone.user.UserService;
import org.example.imdbclone.user.dto.UserCreateDto;
import org.example.imdbclone.user.dto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository users;
    private final UserService userService;
    private final PasswordEncoder passwords;
    private final JwtService jwt;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody UserCreateDto dto) {
        UserResponseDto user = userService.createUser(dto);
        return new AuthResponse(jwt.generateToken(user.userId()), user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        var user = users.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!passwords.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new AuthResponse(jwt.generateToken(user.getUserId()),
                userService.getUserById(user.getUserId()));
    }

    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {
        return userService.getUserById(Long.valueOf(authentication.getName()));
    }
}
