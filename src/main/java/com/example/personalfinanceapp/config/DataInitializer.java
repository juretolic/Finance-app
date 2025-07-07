package com.example.personalfinanceapp.config;

import com.example.personalfinanceapp.model.User;
import com.example.personalfinanceapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.Role.ADMIN);
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully!");
        }
        
        // Create a sample user if no users exist
        if (userRepository.count() == 1) { // Only admin exists
            User sampleUser = new User();
            sampleUser.setUsername("user");
            sampleUser.setEmail("user@example.com");
            sampleUser.setPassword(passwordEncoder.encode("user123"));
            sampleUser.setRole(User.Role.USER);
            userRepository.save(sampleUser);
            System.out.println("Sample user created successfully!");
        }
    }
} 