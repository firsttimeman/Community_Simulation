package zerobaseproject.community.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import zerobaseproject.community.security.repository.RefreshRepository;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomLogoutHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 쿠키에서 refresh 토큰 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // 토큰이 존재하는지 확인
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 만료 여부 확인 (isExpired 메서드 활용)
        if (jwtUtil.isExpired(refresh)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰의 서명을 확인하여 변조 여부 확인 (try-catch 활용)
        try {
            jwtUtil.getCategory(refresh); // 토큰을 파싱해서 예외가 발생하지 않으면 유효
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB에서 토큰 삭제
        if (refreshRepository.existsByRefresh(refresh)) {
            refreshRepository.deleteByRefresh(refresh);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 쿠키 무효화
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
