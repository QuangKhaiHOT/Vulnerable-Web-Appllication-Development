package com.clothes.clothes.model;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // USER hoặc ADMIN
    private String email;
    private String avtUrl;

    public User(Long id, String username, String password, String role, String email , String avtUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.avtUrl = avtUrl;

    }
}
