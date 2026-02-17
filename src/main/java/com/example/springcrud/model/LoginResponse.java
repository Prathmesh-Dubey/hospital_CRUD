package com.example.springcrud.model;

public class LoginResponse {

    private String message;
    private String userId;
    private String name;
    private String role;

    public LoginResponse(String message, String userId, String name, String role) {
        this.message = message;
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
