package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository repository;


    @Test
    public void shouldCreateUser() {

        User user = new User();
        user.setUsername("name");
        user.setPassword("password");

        userService.create(user);
        verify(repository, times(1)).save(user);
    }

    @Test
    public void shouldFailWhenUserAlreadyExists() {

        User user = new User();
        user.setUsername("name");
        user.setPassword("password");

        when(repository.findById(user.getUsername())).thenReturn(Optional.of(new User()));
        assertThrows(IllegalArgumentException.class, () -> userService.create(user));
    }
}
