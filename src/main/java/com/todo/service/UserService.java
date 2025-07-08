package com.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.DTO.ChangeEmailRequestDTO;
import com.todo.DTO.ChangePasswordRequestDTO;
import com.todo.DTO.UserProfileResponseDTO;
import com.todo.entity.User;
import com.todo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void updateEmail(User user, ChangeEmailRequestDTO request) {
        String newEmail = request.getNewEmail();
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }
        //check new email is not empty
        if (newEmail == null || newEmail.isEmpty()) {
            throw new RuntimeException("New email cannot be empty");
        }
        //check email format
        if (!newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Invalid email format");
        }
        if (newEmail.equals(user.getEmail())) {
            throw new RuntimeException("New email cannot be the same as the old email");
        }
        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already exists");
        }
        user.setEmail(newEmail);
        userRepository.save(user);
    }
    public void updatePassword(User user, ChangePasswordRequestDTO request) {
        String newPassword = request.getNewPassword();
        String oldPassword = request.getOldPassword();
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new RuntimeException("Old password cannot be empty");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("New password cannot be empty");
        }
        if (newPassword.length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters long");
        }
        if (newPassword.equals(oldPassword)) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }
        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
