package org.example.imdbclone.user;

import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.dto.UserCreateDto;
import org.example.imdbclone.user.dto.UserResponseDto;
import org.example.imdbclone.user.dto.UserUpdateDto;
import org.example.imdbclone.user.exception.UserAlreadyExistsException;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                         .userId(99999999L)
                         .username("jane_doe")
                         .email("janedoe@kds.pl")
                         .passwordHash("password")
                         .createdAt(LocalDateTime.now())
                         .build();
    }

    @Nested
    @DisplayName("[USER] SERVICE: CREATE")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully when username and email are unique")
        void shouldCreateUserSuccessfully() {
            UserCreateDto createDto = new UserCreateDto("jane_doe", "janedoe@kds.pl", "password");

            when(userRepository.existsByUsername("jane_doe")).thenReturn(false);
            when(userRepository.existsByEmail("janedoe@kds.pl")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(sampleUser);

            UserResponseDto response = userService.createUser(createDto);

            assertThat(response).isNotNull();
            assertThat(response.userId()).isEqualTo(99999999L);
            assertThat(response.username()).isEqualTo("jane_doe");
            assertThat(response.email()).isEqualTo("janedoe@kds.pl");

            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw UserAlreadyExistsException when username is already taken")
        void shouldThrowExceptionWhenUsernameIsTaken() {
            UserCreateDto createDto = new UserCreateDto("jane_doe", "other@example.com", "password");
            when(userRepository.existsByUsername("jane_doe")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(createDto))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("Username 'jane_doe' is already taken");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw UserAlreadyExistsException when email is already registered")
        void shouldThrowExceptionWhenEmailIsTaken() {
            UserCreateDto createDto = new UserCreateDto("new_user", "janedoe@kds.pl", "password");
            when(userRepository.existsByUsername("new_user")).thenReturn(false);
            when(userRepository.existsByEmail("janedoe@kds.pl")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(createDto))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("Email 'janedoe@kds.pl' is already registered");

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("[USER] SERVICE: READ")
    class ReadUserTests {

        @Test
        @DisplayName("Should return user DTO when user exists by ID")
        void shouldReturnUserByIdSuccessfully() {
            Long existingId = 99999999L;
            when(userRepository.findById(existingId)).thenReturn(Optional.of(sampleUser));

            UserResponseDto response = userService.getUserById(existingId);

            assertThat(response).isNotNull();
            assertThat(response.userId()).isEqualTo(existingId);
            assertThat(response.username()).isEqualTo("jane_doe");
            assertThat(response.email()).isEqualTo("janedoe@kds.pl");
            verify(userRepository, times(1)).findById(existingId);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist by ID")
        void shouldThrowExceptionWhenUserNotFoundById() {
            Long nonExistingId = 11111111L;
            when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(nonExistingId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));
        }

        @Test
        @DisplayName("Should return user DTO when user exists by username")
        void shouldReturnUserByUsernameSuccessfully() {
            String existingUsername = "jane_doe";
            when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(sampleUser));

            UserResponseDto response = userService.getUserByUsername(existingUsername);

            assertThat(response).isNotNull();
            assertThat(response.username()).isEqualTo(existingUsername);
            verify(userRepository, times(1)).findByUsername(existingUsername);
        }

        @Test
        @DisplayName("Should return list of all users")
        void shouldReturnAllUsers() {
            User secondUser = User.builder()
                                  .userId(88888888L)
                                  .username("john_doe")
                                  .email("johndoe@kds.pl")
                                  .passwordHash("password")
                                  .createdAt(LocalDateTime.now())
                                  .build();

            when(userRepository.findAll()).thenReturn(List.of(sampleUser, secondUser));

            List<UserResponseDto> result = userService.getAllUsers();

            assertThat(result).isNotNull()
                              .hasSize(2);
            assertThat(result.get(0)
                             .username()).isEqualTo("jane_doe");
            assertThat(result.get(1)
                             .username()).isEqualTo("john_doe");
            verify(userRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("[USER] SERVICE: UPDATE(PUT)")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user successfully when data is valid")
        void shouldUpdateUserSuccessfully() {
            Long existingId = 99999999L;
            UserUpdateDto updateDto = new UserUpdateDto("jane_updated", "updated@kds.pl");

            when(userRepository.findById(existingId)).thenReturn(Optional.of(sampleUser));
            when(userRepository.existsByUsername("jane_updated")).thenReturn(false);
            when(userRepository.existsByEmail("updated@kds.pl")).thenReturn(false);

            UserResponseDto result = userService.updateUser(existingId, updateDto);

            assertThat(result).isNotNull();
            assertThat(result.username()).isEqualTo("jane_updated");
            assertThat(result.email()).isEqualTo("updated@kds.pl");
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when updating non-existing user")
        void shouldThrowExceptionWhenUpdatingNonExistingUser() {
            Long nonExistingId = 11111111L;
            UserUpdateDto updateDto = new UserUpdateDto("test", "test@kds.pl");

            when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(nonExistingId, updateDto))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));
        }

        @Test
        @DisplayName("Should throw UserAlreadyExistsException when new username is already taken by someone else")
        void shouldThrowExceptionWhenNewUsernameIsTaken() {
            Long existingId = 99999999L;
            UserUpdateDto updateDto = new UserUpdateDto("taken_user", "valid@kds.pl");

            when(userRepository.findById(existingId)).thenReturn(Optional.of(sampleUser));
            when(userRepository.existsByUsername("taken_user")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(existingId, updateDto))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("Username 'taken_user' is already taken");
        }
    }

    @Nested
    @DisplayName("[USER] SERVICE: DELETE")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully when user exists")
        void shouldDeleteUserSuccessfully() {
            Long existingId = 99999999L;
            when(userRepository.existsById(existingId)).thenReturn(true);

            userService.deleteUser(existingId);

            verify(userRepository, times(1)).deleteById(existingId);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when deleting non-existing user")
        void shouldThrowExceptionWhenDeletingNonExistingUser() {
            Long nonExistingId = 11111111L;
            when(userRepository.existsById(nonExistingId)).thenReturn(false);

            assertThatThrownBy(() -> userService.deleteUser(nonExistingId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.valueOf(nonExistingId));

            verify(userRepository, never()).deleteById(nonExistingId);
        }
    }
}