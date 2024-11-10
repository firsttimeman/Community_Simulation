package zerobaseproject.community.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.member.entity.Member;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWTFilter is processing a request.");

        // 표준 Authorization 헤더에서 JWT 토큰 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Authorization 헤더가 없거나 형식이 잘못된 경우 다음 필터로 넘김
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 접두사 이후의 토큰만 추출
        String accessToken = authorizationHeader.substring(7);

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("access token expired");
            return;
        }

        // 토큰의 category가 "access"인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!"access".equals(category)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("invalid access token");
            return;
        }

        // email, role 정보 추출
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Member member = Member.builder()
                .email(email)
                .userRoles(UserRoles.valueOf(role))  // role을 Enum으로 변환
                .build();

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member, null, authorities);

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Parsed email: {}, role: {}", email, role);

        // 다음 필터로 요청 넘김
        filterChain.doFilter(request, response);
    }
}
