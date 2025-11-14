package com.example.hello_spring.service;

import com.example.hello_spring.dto.auth.AuthRequest;
import com.example.hello_spring.dto.auth.AuthResponse;
import com.example.hello_spring.dto.auth.RegisterRequest;
import com.example.hello_spring.entity.User;
import com.example.hello_spring.exception.DuplicateException;
import com.example.hello_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * 1. Đăng ký (REGISTER)
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra xem user đã tồn tại chưa
        List<String> existingUsers = new ArrayList<>();

        if (userRepository.existsByUsername(request.username())) {
            existingUsers.add("Username already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            existingUsers.add("Email already taken");
        }

        if (!existingUsers.isEmpty()) {
            throw new DuplicateException(existingUsers);
        }

        // Tạo User mới
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                // Mã hóa password trước khi lưu
                .password(passwordEncoder.encode(request.password()))
                // Chúng ta sẽ thêm "Role" ở đây sau
                .build();

        // Lưu user vào CSDL
        userRepository.save(user);

        // Tạo JWT token
        String jwtToken = jwtService.generateToken(user);

        // Trả về token
        return new AuthResponse(request.username(), jwtToken);
    }

    /**
     * 2. Đăng nhập (LOGIN / AUTHENTICATE)
     */
    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        // 1. Xác thực user (username + password)
        //    AuthenticationManager sẽ dùng AuthenticationProvider (trong ApplicationConfig)
        //    để kiểm tra username/password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Nếu không có lỗi, user đã được xác thực
        //    Tìm lại user trong CSDL (để tạo token)
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. Tạo JWT token
        String jwtToken = jwtService.generateToken(user);

        // 4. Trả về token
        return new AuthResponse(request.username(), jwtToken);
    }
}
