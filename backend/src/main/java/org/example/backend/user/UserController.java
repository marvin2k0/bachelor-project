package org.example.backend.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.mappers.UserMapper;
import org.example.backend.user.dto.UserCreationDto;
import org.example.backend.user.dto.UserDto;
import org.example.backend.user.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(sort = "id") Pageable pageable) {
        final Page<User> page = service.getAllUsers(pageable);
        return ResponseEntity.ok(page.map(this.mapper::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Long id) {
        final User user = service.getUserById(id);
        return ResponseEntity.ok(this.mapper.toDto(user));
    }

    @PostMapping
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