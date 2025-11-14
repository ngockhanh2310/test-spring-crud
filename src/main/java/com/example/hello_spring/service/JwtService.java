package com.example.hello_spring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY;

    public JwtService(@Value("${jwt.secret.key}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    // 3. lấy getSignInKey() từ SECRET_KEY
    private Key getSignInKey() {
        // Dùng byte của chuỗi String bí mật
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. Tạo Token (Hàm chính)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) { // payload
        return Jwts.builder()
                // --- PAYLOAD (NỘI DUNG VÉ) ---
                .setClaims(extraClaims) // (Payload tùy chỉnh nếu có)
                .setSubject(userDetails.getUsername()) // <-- {"sub": "linhngo"}
                .setIssuedAt(new Date(System.currentTimeMillis())) // <-- {"iat": ...}
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // <-- {"exp": ...}
                // --- KẾT THÚC PAYLOAD ---

                // --- HEADER (LOẠI VÉ) & SIGNATURE ---
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // <-- {"alg": "HS256"}
                // --- KẾT THÚC HEADER & SIGNATURE ---
                .compact();
    }

    // 3. Trích xuất Username (Subject) từ Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. Kiểm tra Token có hợp lệ không
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        // --- ĐÂY LÀ "KIỂM TRA VÉ" ---
        // Hàm "parserBuilder()" này làm 2 việc:
        // 1. Nó "dịch" (decode) Header và Payload.
        // 2. Nó dùng "getSignInKey()" (chìa khóa bí mật)
        //    để "kiểm tra chữ ký" (Signature).
        //
        // (Nếu chữ ký giả/vé hết hạn, nó sẽ NÉM LỖI ngay tại đây)
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
