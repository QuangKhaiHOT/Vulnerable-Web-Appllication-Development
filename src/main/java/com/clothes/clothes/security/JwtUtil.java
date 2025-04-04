package com.clothes.clothes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;
import org.jose4j.jws.JsonWebSignature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {
    private static final String SECRET = "NoOneKnows";
  /*  //Tạo JWT
    public String generateToken(String email, long expiryDuration) {
        try {
            // Tạo Header
            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = base64UrlEncode(headerJson);

            // Tạo Payload
            long expirationTime = System.currentTimeMillis() + expiryDuration;
            String payloadJson = "{\"sub\":\"" + email + "\",\"iat\":" + System.currentTimeMillis() + ",\"exp\":" + expirationTime + "}";
            String encodedPayload = base64UrlEncode(payloadJson);

            // Tạo Signature
            String signature = generateHmacSHA256(encodedHeader + "." + encodedPayload, SECRET);

            // Trả về token
            return encodedHeader + "." + encodedPayload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo JWT", e);
        }
    }

    // Mã hóa base64 URL
    private String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo chữ ký HMAC-SHA256
    private String generateHmacSHA256(String data, String secret) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }*/
    //Tạo JWT
    public String generateToken(String email, long expiryDuration) {
        System.out.println(SECRET);
        return Jwts.builder()
                .setSubject(String.valueOf(email))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiryDuration))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    //Giải mã JWT
    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
    //Lấy thông tin từ JWT
    public String getEmailFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    //Kiểm tra JWT có hợp lệ không
    public boolean validateToken(String token) {
        try {
            return decodeToken(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
