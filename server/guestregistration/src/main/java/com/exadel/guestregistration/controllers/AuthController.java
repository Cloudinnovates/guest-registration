package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.models.User;
import com.exadel.guestregistration.payloads.LoginRequest;
import com.exadel.guestregistration.payloads.SignUpRequest;
import com.exadel.guestregistration.security.CustomUserDetailsService;
import com.exadel.guestregistration.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;




    /**
     * Authenticates user and sends authentication token (JWT) as a response
     * @param loginRequest
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest ){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().body(null);
    }

    /**
     * creates new user if parameters are correct and user with provided username does not exist
     * @param signUpRequest
     * @return ResponseEntity
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userDetailsService.usernameExists(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken");
        }
        User user = new User( signUpRequest.getUsername(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));


        // might change in the future
//        user.setRole(Role.ADMIN);

        User result = userDetailsService.saveUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body( "User registered successfully");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok().body("logout successful");
    }

    @GetMapping("/current_user")
    public ResponseEntity<?> getCurrentUser(HttpSession session){
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        UserPrincipal principal = (UserPrincipal) context.getAuthentication().getPrincipal();
        return ResponseEntity.ok().body("{\"username\": \"" + principal.getUsername() + "\"}");
    }

}
