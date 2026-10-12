package org.example.imdbclone.security;

import org.example.imdbclone.user.UserRepository;
import org.example.imdbclone.user.UserService;
import org.example.imdbclone.user.domain.User;
import org.example.imdbclone.user.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PasswordStorageTest {
    @Test void registrationStoresARealBcryptHash() {
        var repository = mock(UserRepository.class);
        var encoder = new BCryptPasswordEncoder(12);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        new UserService(repository, encoder).createUser(new UserCreateDto("alice", "alice@example.com", "password123"));
        var saved = ArgumentCaptor.forClass(User.class);
        verify(repository).save(saved.capture());
        String hash = saved.getValue().getPasswordHash();
        assertThat(hash).isNotEqualTo("password123");
        assertThat(encoder.matches("password123", hash)).isTrue();
        assertThat(encoder.matches("wrongpassword", hash)).isFalse();
    }
}
