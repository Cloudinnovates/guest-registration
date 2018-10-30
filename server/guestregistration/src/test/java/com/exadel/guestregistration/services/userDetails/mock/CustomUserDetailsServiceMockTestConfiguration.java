package com.exadel.guestregistration.services.userDetails.mock;

import com.exadel.guestregistration.repositories.UserRepository;
import com.exadel.guestregistration.security.CustomUserDetailsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsServiceMockTestConfiguration {

    @Bean
    public CustomUserDetailsService customUserDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRepository userRepository(){
        return Mockito.mock(UserRepository.class);
    }
}
