package org.example.imdbclone.user;

import lombok.RequiredArgsConstructor;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.dto.UserCreateDto;
import org.example.imdbclone.user.dto.UserPatchDto;
import org.example.imdbclone.user.dto.UserResponseDto;
import org.example.imdbclone.user.dto.UserUpdateDto;
import org.example.imdbclone.user.exception.UserAlreadyExistsException;
import org.example.imdbclone.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
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

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponseDto(user);
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new UserNotFoundException(username));
        return mapToResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(this::mapToResponseDto)
                             .toList();
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getUsername()
                 .equals(dto.username()) && userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("Username '" + dto.username() + "' is already taken");
        }
        if (!user.getEmail()
                 .equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Email '" + dto.email() + "' is already registered");
        }
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        return mapToResponseDto(user);
    }

    @Transactional
    public UserResponseDto patchUser(Long id, UserPatchDto dto){
        User user=userRepository.findById(id).
                orElseThrow(()->new UserNotFoundException(id));

        if (dto.username() != null && !dto.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.username())) {
                throw new UserAlreadyExistsException("Username '" + dto.username() + "' is already taken");
            }
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new UserAlreadyExistsException("Email '" + dto.email() + "' is already registered");
            }
            user.setEmail(dto.email());
        }

        return mapToResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
