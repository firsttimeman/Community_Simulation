package zerobaseproject.community.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import zerobaseproject.community.auth.jwt.JwtProvider;
import zerobaseproject.community.auth.repository.TokenRepository;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.TokenException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = this.resolveTokenFromRequest(request);


        try {
            if (StringUtils.hasText(token)) {
                String email = jwtProvider.getUserEmail(token);

                // 블랙리스트에 등록된 토큰 확인
                if (tokenRepository.isTokenInBlacklist(email, token)) {
                    log.error("블랙리스트에 등록된 토큰으로 접근 시도");
                    throw new TokenException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
                }

                if (jwtProvider.validateToken(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Authentication 성공: {}", auth.getName());
                } else {
                    throw new TokenException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
                }
            }

            filterChain.doFilter(request, response);

        } catch (TokenException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":\"" + ex.getErrorCode().name() + "\", \"message\":\"" + ex.getErrorCode().getDescription() + "\"}");
        }
    }

    /**
     * 들어온 요청 헤더에서 토큰 정보 추출
     *
     * @param request Http 요청 객체
     * @return 추출된 JWT 토큰 (없으면 null)
     */
    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
