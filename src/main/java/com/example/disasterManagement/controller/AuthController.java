package com.example.disasterManagement.controller;

import com.example.disasterManagement.model.ERole;
import com.example.disasterManagement.model.Role;
import com.example.disasterManagement.model.User;
import com.example.disasterManagement.payload.request.LoginRequest;
import com.example.disasterManagement.payload.request.SignupRequest;
import com.example.disasterManagement.payload.response.JwtResponse;
import com.example.disasterManagement.payload.response.MessageResponse;
import com.example.disasterManagement.repository.RoleRepository;
import com.example.disasterManagement.repository.UserRepository;
import com.example.disasterManagement.security.jwt.JwtUtils;
import com.example.disasterManagement.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        // Set additional fields if provided
        if (signUpRequest.getFullName() != null) {
            user.setFullName(signUpRequest.getFullName());
        }
        
        if (signUpRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
        }

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_AFFECTED_PERSON)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "donor":
                        Role donorRole = roleRepository.findByName(ERole.ROLE_DONOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(donorRole);
                        break;
                    case "volunteer":
                        Role volunteerRole = roleRepository.findByName(ERole.ROLE_VOLUNTEER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(volunteerRole);
                        break;
                    default:
                        Role affectedRole = roleRepository.findByName(ERole.ROLE_AFFECTED_PERSON)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(affectedRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
} 