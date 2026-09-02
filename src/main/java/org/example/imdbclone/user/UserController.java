package org.example.imdbclone.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.imdbclone.user.dto.UserCreateDto;
import org.example.imdbclone.user.dto.UserPatchDto;
import org.example.imdbclone.user.dto.UserResponseDto;
import org.example.imdbclone.user.dto.UserUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserCreateDto dto){
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patchUser(@PathVariable Long id, @RequestBody @Valid UserPatchDto dto){
        return userService.patchUser(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
