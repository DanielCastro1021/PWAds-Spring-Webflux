package com.example.springangularadsapp.security.controller;

import com.example.springangularadsapp.security.authorization.annotation.AdminAccess;
import com.example.springangularadsapp.security.authorization.annotation.ModeratorAccess;
import com.example.springangularadsapp.security.authorization.annotation.UserAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @UserAccess
    @GetMapping("/user")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @ModeratorAccess
    @GetMapping("/mod")
    //@PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @AdminAccess
    @GetMapping("/admin")
    //@PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
