package com.example.hello_spring.config;

import com.example.hello_spring.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Bean này từ ApplicationConfig

    // Đây là hàm "soát vé"
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy "Authorization" header
        final String authHeader = request.getHeader("Authorization");

        // 2. Kiểm tra xem header có tồn tại hoặc có bắt đầu bằng "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Nếu không, cho đi tiếp (không làm gì)
            return;
        }

        // 3. Lấy Token từ header (cắt bỏ "Bearer ")
        final String jwt = authHeader.substring(7);

        // 4. "Giải mã" (extract) username từ Token (dùng JwtService)
        final String username = jwtService.extractUsername(jwt);

        // 5. Kiểm tra (Nếu có username VÀ user này CHƯA được xác thực trong context)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Lấy UserDetails (thông tin user) từ CSDL (dùng UserDetailsService)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 7. Kiểm tra Token có hợp lệ không (dùng JwtService)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. TẠO RA MỘT XÁC THỰC MỚI
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Chúng ta không cần credentials khi dùng JWT
                        userDetails.getAuthorities() // Lấy quyền (Roles)
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. "Báo cáo" cho Spring Security: "User này đã được xác thực"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}
