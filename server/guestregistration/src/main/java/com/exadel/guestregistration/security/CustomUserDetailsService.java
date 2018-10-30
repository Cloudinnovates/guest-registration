package com.exadel.guestregistration.security;

import com.exadel.guestregistration.models.User;
import com.exadel.guestregistration.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * creates UserPrincipal from user data that it retrieves from user service with provided username
     * @param username
     * @return UserDetails
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username){
        User user;
        if(!username.equals(this.username) && userRepository.findUserByName(username).isPresent()) {
            user = userRepository.findUserByName(username).get();
        }else if(username.equals(this.username)){
            user = new User(this.username, passwordEncoder.encode(this.password));
            user.setId("0");
        }else {
            return null;
        }
        return UserPrincipal.create(user);
    }

    public boolean usernameExists(String username) {
        if(!username.equals(this.username)) {
            return userRepository.findUserByName(username).isPresent();
        }else{
            return true;
        }
    }

    public User saveUser(User user) {
        if (!user.getUsername().equals(this.username)) {
            return userRepository.save(user);
        }
        return null;
    }
}
