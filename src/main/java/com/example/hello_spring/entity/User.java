package com.example.hello_spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor // Bắt buộc cho JPA
@AllArgsConstructor // Dùng cho @Builder
@Entity
@Table(name = "users")
public class User implements UserDetails { // <-- Triển khai UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false) // username is final
    private String username;

    @Column(nullable = false)
    private String password; // password (sẽ được mã hóa)

    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    // === CÁC PHƯƠNG THỨC CỦA USERDETAILS ===
    // Spring Security sẽ dùng các hàm này để xác thực

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Chúng ta sẽ làm về "Roles" (Phân quyền) ở đây sau
        // Ví dụ: return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return null; // Tạm thời trả về null
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Tài khoản không bao giờ hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Tài khoản không bao giờ bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mật khẩu không bao giờ hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Tài khoản luôn được kích hoạt
    }
}
