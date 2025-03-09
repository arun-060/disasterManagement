package com.example.disasterManagement.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/donor")
    @PreAuthorize("hasRole('DONOR')")
    public String donorAccess() {
        return "Donor Board.";
    }

    @GetMapping("/volunteer")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerAccess() {
        return "Volunteer Board.";
    }

    @GetMapping("/affected")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedAccess() {
        return "Affected Person Board.";
    }
} 