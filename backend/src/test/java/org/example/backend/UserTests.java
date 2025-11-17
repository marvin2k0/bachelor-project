package org.example.backend;

import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.example.backend.user.User;
import org.example.backend.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTests {
    @Autowired
    private UserService service;

    private User user;

    @AfterEach
    void cleanup() {
        if (user != null && user.getId() != null)
            this.service.deleteUserById(user.getId());

        assertThrows(RuntimeException.class, () -> this.service.getUserById(this.user.getId()));
        this.user = null;
    }

    @Test
    void test_create_user() {
        final String userName = "Max";
        this.user = User.builder()
                .name(userName)
                .build();
        service.saveUser(user);

        assertTrue(service.getAllUsers()
                .stream()
                .anyMatch(s -> s.getName().equals(userName)));
    }

    @Test
    void test_find_user_by_name() {
        final String userName = "Max";
        this.user = User.builder()
                .name(userName)
                .build();
        service.saveUser(user);

        assertNotNull(service.getUserByName(userName));
    }

    @Test
    void test_delete_user_by_name() {
        final String userName = "Max";
        this.user = User.builder()
                .name(userName)
                .build();
        service.saveUser(user);

        service.deleteUserByName(userName);

        assertThrows(RuntimeException.class,
                () -> service.getUserByName(userName));
    }
}
