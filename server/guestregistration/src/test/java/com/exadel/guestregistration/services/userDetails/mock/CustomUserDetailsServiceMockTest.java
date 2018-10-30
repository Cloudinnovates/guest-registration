package com.exadel.guestregistration.services.userDetails.mock;

import com.exadel.guestregistration.models.User;
import com.exadel.guestregistration.repositories.UserRepository;
import com.exadel.guestregistration.security.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CustomUserDetailsServiceMockTestConfiguration.class)
public class CustomUserDetailsServiceMockTest {
    private static final String USERNAME = "username";
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setup(){
        Mockito.when(userRepository.findUserByName(USERNAME)).thenReturn(Optional.of(new User()));
    }

    @Test
    public void testLoadedServiceAndRepositories(){
        assertNotNull(userDetailsService);
        assertNotNull(userRepository);

        assertTrue(userRepository.getClass().getSimpleName().contains("$MockitoMock$"));
    }

    @Test
    public void test_createUser() throws Exception {
        User user = new User();
        user.setPassword("test_password");
        user.setUsername("test_username");

        userDetailsService.saveUser(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void test_usernameExists() throws Exception {
        assertTrue(userDetailsService.usernameExists(USERNAME));
    }

    @Test
    public void test_loadUserByUsername() throws Exception {
        assertNotNull(userDetailsService.loadUserByUsername(USERNAME));
    }
}
