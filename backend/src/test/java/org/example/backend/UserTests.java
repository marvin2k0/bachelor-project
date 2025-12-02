package org.example.backend;

import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.example.backend.user.UserService;
import org.example.backend.user.dto.UserCreationDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTests {
    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @AfterEach
    void cleanup() {
        if (user != null && user.getId() != null)
            this.service.deleteUserById(user.getId());

        assertThrows(RuntimeException.class, () -> this.service.getUserById(this.user.getId()));
        this.user = null;
        this.userRepository.deleteAll();
    }

    @Test
    void test_create_user() {
        final String userName = "Max";
        service.createUser(new UserCreationDto(userName, "1111111", userName + "@gmail.com"));

        assertTrue(service.getAllUsers()
                .stream()
                .anyMatch(s -> s.getUsername().equals(userName)));
    }

    @Test
    void test_find_user_by_name() {
        final String userName = "Max";
        service.createUser(new UserCreationDto(userName, "1111112",userName + "@gmail.com"));

        assertNotNull(service.getUserByName(userName));
    }

    @Test
    void test_delete_user_by_name() {
        final String userName = "Max_Delete_Test";
        service.createUser(new UserCreationDto(userName, "1111113",userName + "@gmail.com"));

        final Optional<User> optionalUser = service.deleteUserByName(userName);

        assertTrue(optionalUser.isPresent());
        assertEquals(userName, optionalUser.get().getUsername());
    }
}
