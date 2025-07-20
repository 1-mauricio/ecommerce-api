package com.ecommerce.dto.auth;

import com.ecommerce.enums.UserRole;

import java.util.UUID;

public class AuthResponseDTO {
    private String token;
    
    private String type = "Bearer";
    
    private UUID userId;
    
    private String email;
    
    private String name;
    
    private UserRole role;
    
    public AuthResponseDTO(String token, UUID userId, String email, String name, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
