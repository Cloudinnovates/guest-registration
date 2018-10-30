package com.exadel.guestregistration.services.userDetails;

import com.exadel.guestregistration.models.User;
import com.exadel.guestregistration.repositories.UserRepository;
import com.exadel.guestregistration.security.CustomUserDetailsService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setup(){
        Assert.assertNotNull(userDetailsService);
        Assert.assertNotNull(userRepository);
    }

    @After
    public void clean(){
        userRepository.deleteAll();
    }

    private User createNewUser(){
        return new User("username", "password");
    }

    @Test
    public void saveUserTest() throws Exception{
        User user = createNewUser();
        String userId = userDetailsService.saveUser(user).getId();
        User savedUser = userRepository.findById(userId).get();
        Assert.assertEquals(user.getUsername(), savedUser.getUsername());
        Assert.assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    public void usernameExistsTest() throws Exception{
        User user = createNewUser();
        userRepository.save(user);
        Assert.assertTrue(userDetailsService.usernameExists(user.getUsername()));
        Assert.assertFalse(userDetailsService.usernameExists("test"));
    }

    @Test
    public void loadUserByUsernameTest() throws Exception{
        User user = createNewUser();
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Assert.assertEquals(userDetails.getUsername(), user.getUsername());
        Assert.assertEquals(userDetails.getPassword(), user.getPassword());

    }
}
