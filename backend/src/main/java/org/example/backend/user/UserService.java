package org.example.backend.user;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.example.backend.mappers.UserMapper;
import org.example.backend.user.dto.UserCreationDto;
import org.example.backend.user.dto.UserUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User createUser(UserCreationDto userCreationDto) {
        final User user = this.mapper.toEntity(userCreationDto);
        return this.repository.save(user);
    }

    public User getUserById(long id) {
        final Optional<User> optional = repository.findById(id);
        User entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for id:" + id);
        }
        return entity;
    }

    public User getUserByName(String name) {
        final Optional<User> optional = repository.findByUsername(name);
        User entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for name:" + name);
        }
        return entity;
    }

    public void deleteUserById(long id) {
        this.repository.deleteById(id);
    }

    public Optional<User> deleteUserByName(String name) {
        return this.repository.deleteByUsername(name);
    }

    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("User not found for this id: " + id));
        this.mapper.updateEntity(userUpdateDto, user);

        return this.repository.save(user);
    }
}