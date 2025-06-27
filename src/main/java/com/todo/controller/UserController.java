package com.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.DTO.UserProfileDTO;
import com.todo.entity.User;
import com.todo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        // Hoặc bạn có thể dùng @AuthenticationPrincipal
        User user = (User) authentication.getPrincipal();
        UserProfileDTO profile = new UserProfileDTO(user);
        return ResponseEntity.ok(profile);
    }
}
