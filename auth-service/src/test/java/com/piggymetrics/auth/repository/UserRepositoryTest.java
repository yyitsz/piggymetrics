package com.piggymetrics.auth.repository;

import com.piggymetrics.auth.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void shouldSaveAndFindUserByName() {

        User user = new User();
        user.setUsername("name");
        user.setPassword("password");
        repository.save(user);

        Optional<User> found = repository.findById(user.getUsername());
        assertTrue(found.isPresent());
        assertEquals(user.getUsername(), found.get().getUsername());
        assertEquals(user.getPassword(), found.get().getPassword());
    }
}
