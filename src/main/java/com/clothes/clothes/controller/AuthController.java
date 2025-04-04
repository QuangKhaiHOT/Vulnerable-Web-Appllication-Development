package com.clothes.clothes.controller;

import com.clothes.clothes.model.User;
import com.clothes.clothes.repository.UserRepository;
import com.clothes.clothes.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/verify") // API xác thực có đường dẫn /auth/verify
    public ResponseEntity<?> verifyToken(@CookieValue(value = "refresh_token", required = false) String token) {
        try {
            // Tạo một instance của JwtUtil để kiểm tra token
            JwtUtil jwtUtil = new JwtUtil();

            // Nếu token hợp lệ, trả về HTTP 200 OK
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getEmailFromToken(token); // Lấy email từ token
                User user = UserRepository.getUserByEmail(email); // Lấy thông tin user từ email

                if(user != null) {
                    String accessToken = jwtUtil.generateToken(email, 15 * 60 * 1000); // 15 phút
                    return ResponseEntity.ok(Map.of("access_token", accessToken, "user", user , "role", user.getRole()));
                }else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("error", "User không tồn tại"));
                }
            }
            // Nếu token không hợp lệ, trả về HTTP 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            // Bắt lỗi và trả về HTTP 401 Unauthorized nếu có lỗi xảy ra
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response) {

        User user = UserRepository.loginUser(email, password);
        if (user != null) {
            // Tạo JWT tokens
            JwtUtil jwtUtil = new JwtUtil();
            String accessToken = jwtUtil.generateToken(email, 15 * 60 * 1000); // 15 phút
            String refreshToken = jwtUtil.generateToken(email, 7 * 24 * 60 * 60 * 1000); // 7 ngày

            // Tạo cookie cho refresh token
            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true); // Bảo mật, không cho JavaScript truy cập
            refreshCookie.setSecure(false); // Set true nếu dùng HTTPS
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(refreshCookie);// Lưu Refresh Token vào Cookie

            // Trả về redirect URL dựa vào role
            String redirectUrl = "client".equals(user.getRole())
                ? "/home"
                : "/admin";
            //Lấy thông tin user qua email
            User userInfo = UserRepository.getUserByEmail(email); // Lấy thông tin user từ email
            return ResponseEntity.ok()
                .body(Map.of("redirect", redirectUrl, "access_token", accessToken,"user", userInfo));
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid credentials"));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Tạo cookie mới với thời gian hết hạn ngay lập tức
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Hết hạn ngay lập tức
        response.addCookie(cookie);

        return ResponseEntity.ok(Collections.singletonMap("message", "Đã đăng xuất"));
    }
    // Regex kiểm tra email hợp lệ
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$";

    @PostMapping("/regis")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        // Kiểm tra username đã tồn tại
        if (UserRepository.usernameExists(username)) {
            return "redirect:/register?error=username_used";
        }
        //Kiểm tra email đã tồn tại
        if (UserRepository.emailExists(email)) {
            return "redirect:/register?error=email_used";
        }

        boolean success = UserRepository.addUser(username, password, email);

        if (success) {
            return "redirect:/login";
        } else {
            return "redirect:/register?error=general&message=Đăng ký thất bại";
        }
    }
}

