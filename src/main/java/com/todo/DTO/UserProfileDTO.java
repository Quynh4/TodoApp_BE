package com.todo.DTO;

import com.todo.entity.User;

public class UserProfileDTO {
    private String username;
    private String email;
    public UserProfileDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
}
