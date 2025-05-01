package com.example.auth.service;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Updated register method with email as a parameter
    public User register(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);  // Set username
        user.setPassword(passwordEncoder.encode(password));  // Encode the passwor
        user.setRole(role);  // Set user role

        return userRepository.save(user);
    }

    // For simplicity, dummy implementations for forgot/reset
//    public void forgotPassword(String email) {
//        // Implement email sending logic for forgot password
//        emailService.sendPasswordResetEmail(email);
//    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));  // Update password
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())  // Use email as the username
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}
