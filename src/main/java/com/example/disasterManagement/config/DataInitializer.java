package com.example.disasterManagement.config;

import com.example.disasterManagement.model.ERole;
import com.example.disasterManagement.model.Role;
import com.example.disasterManagement.model.User;
import com.example.disasterManagement.repository.RoleRepository;
import com.example.disasterManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        initRoles();
        
        // Initialize default users if they don't exist
        initUsers();
    }

    private void initRoles() {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            // Create roles
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            Role donorRole = new Role(ERole.ROLE_DONOR);
            Role volunteerRole = new Role(ERole.ROLE_VOLUNTEER);
            Role affectedRole = new Role(ERole.ROLE_AFFECTED_PERSON);

            // Save roles
            roleRepository.save(adminRole);
            roleRepository.save(donorRole);
            roleRepository.save(volunteerRole);
            roleRepository.save(affectedRole);

            System.out.println("Roles initialized successfully");
        }
    }

    private void initUsers() {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Create admin user
            createUser("admin", "admin123", ERole.ROLE_ADMIN);
            
            // Create donor user
            createUser("donor", "donor123", ERole.ROLE_DONOR);
            
            // Create volunteer user
            createUser("volunteer", "volunteer123", ERole.ROLE_VOLUNTEER);
            
            // Create affected person user
            createUser("affected", "affected123", ERole.ROLE_AFFECTED_PERSON);

            System.out.println("Default users initialized successfully");
        }
    }

    private void createUser(String username, String password, ERole roleEnum) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPassword(passwordEncoder.encode(password));
        
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        roles.add(role);
        user.setRoles(roles);
        
        userRepository.save(user);
    }
} 