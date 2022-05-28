package com.example.reactivepwads.security.controller;

import com.example.reactivepwads.security.util.JwtUtils;
import com.example.reactivepwads.security.util.PBKDF2Encoder;
import com.example.reactivepwads.security.model.*;
import com.example.reactivepwads.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.*;

@AllArgsConstructor
@RestController
public class AuthenticationREST {

    private JwtUtils jwtUtil;
    private PBKDF2Encoder passwordEncoder;
    private UserService userService;


    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername()).filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())).map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)))).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        Mono<Boolean> username = userService.existsByUsername(signUpRequest.getUsername());
        Mono<Boolean> email = userService.existsByEmail(signUpRequest.getEmail());

        return Mono.zip(username, email).flatMap(objects -> {
            if (Boolean.TRUE.equals(objects.getT1())) {
                return (Mono.just(ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"))));
            } else if (Boolean.TRUE.equals(objects.getT2())) {
                return (Mono.just(ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"))));
            } else {
                // Create new user's account
                User user = new User(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), true);

                List<String> strRoles = signUpRequest.getRoles();
                List<Role> roles = new ArrayList<>();

                if (strRoles == null) {
                    roles.add(Role.ROLE_USER);
                } else {
                    strRoles.forEach(role -> {
                        if ("admin".equals(role)) {
                            roles.add(Role.ROLE_ADMIN);
                        } else {
                            roles.add(Role.ROLE_USER);
                        }
                    });
                }

                user.setRoles(roles);
                return userService.save(user).then(Mono.just(ResponseEntity.ok(new MessageResponse("User registered successfully!"))));
            }
        });
    }
}

