package org.example.backend.user;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User entity) {
        return this.repository.save(entity);
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

    public void deleteUserById(long id) {
        this.repository.deleteById(id);
    }

    public User updateUser(Long id, User entity) {

        final User entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("User not found for this id :: " + id));

        entityFromDb.setId(id);
        this.repository.save(entityFromDb);
        return entityFromDb;
    }
}