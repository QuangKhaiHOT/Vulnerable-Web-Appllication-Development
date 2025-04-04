package com.clothes.clothes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class ViewController {
    @GetMapping("/")
    public String showAuthPage() {
        return "auth";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
    @GetMapping("/products")
    public String showProductAdminPage() {
        return "productAdmin";
    }
    @GetMapping("/user")
    public String showUserAdminPage() {
        return "userAdmin";
    }
    @GetMapping("/category")
    public String showCategoryAdminPage() {
        return "categoryAdmin";
    }
}
