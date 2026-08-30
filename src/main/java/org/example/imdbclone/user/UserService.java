package org.example.imdbclone.user;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.dto.UserCreateDTO;
import org.example.imdbclone.user.dto.UserResponseDTO;
import org.example.imdbclone.user.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("Username '" + dto.username() + "' is already taken");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Email '" + dto.email() + "' is already registered");
        }

        //TODO: passwordEncoder.encode(dto.password())
        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .passwordHash(dto.password()) // placeholder
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    private UserResponseDTO mapToResponseDto(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
