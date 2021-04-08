package com.piggymetrics.auth.service.security;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private PiggyUserDetailsService service;

    @Mock
    private UserRepository repository;


    @Test
    public void shouldLoadByUsernameWhenUserExists() {

        final User user = new User();

        when(repository.findById(any())).thenReturn(Optional.of(user));
        UserDetails loaded = service.loadUserByUsername("name");

        assertEquals(user, loaded);
    }

    @Test
    public void shouldFailToLoadByUsernameWhenUserNotExists() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("name"));
    }
}