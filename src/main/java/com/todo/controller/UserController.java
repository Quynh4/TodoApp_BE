package com.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.DTO.ChangeEmailRequestDTO;
import com.todo.DTO.ChangePasswordRequestDTO;
import com.todo.DTO.UserProfileResponseDTO;
import com.todo.entity.User;
import com.todo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getProfile(Authentication authentication) {
        try{
        User user = (User) authentication.getPrincipal();
        UserProfileResponseDTO profile = new UserProfileResponseDTO(user);
        return ResponseEntity.ok(profile);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
        
    }
    @PostMapping("/change-email")
    public ResponseEntity<String> updateEmail(Authentication authentication,@Valid @RequestBody ChangeEmailRequestDTO request) {
        try{
            User user = (User) authentication.getPrincipal();
            userService.updateEmail(user, request);
            return ResponseEntity.ok("Email updated successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating email: " + e.getMessage());
        }

        
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> updatePassword(Authentication authentication,@Valid @RequestBody ChangePasswordRequestDTO request) {
        try {
            // Thực hiện cập nhật mật khẩu
            // Nếu có lỗi xảy ra, sẽ ném ra RuntimeException
            User user = (User) authentication.getPrincipal();
            userService.updatePassword(user, request);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().body("Error updating password: " + e.getMessage());
        }
        
    }
}
