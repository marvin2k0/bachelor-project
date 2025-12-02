package org.example.backend.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.mappers.UserMapper;
import org.example.backend.user.dto.UserCreationDto;
import org.example.backend.user.dto.UserDto;
import org.example.backend.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        final List<User> users = service.getAllUsers();
        return ResponseEntity.ok(this.mapper.toDto(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Long id) {
        final User user = service.getUserById(id);
        return ResponseEntity.ok(this.mapper.toDto(user));
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserCreationDto userCreationDto) {
        final User user = service.createUser(userCreationDto);
        log.info("User created {}", user);
        return ResponseEntity.ok(this.mapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        final User user = this.service.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(this.mapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}