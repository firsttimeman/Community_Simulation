package zerobaseproject.community.security.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zerobaseproject.community.security.entity.RefreshEntity;
import zerobaseproject.community.security.jwt.JWTUtil;
import zerobaseproject.community.security.repository.RefreshRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public Map<String, String> reissueTokens(String refreshToken) {
        // 만료 여부 확인
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        // 토큰이 refresh 인지 확인
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {

            //response body
           throw new IllegalArgumentException("Invalid refresh token");
        }

        // username과 role 추출
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);



        // 새로운 access와 refresh 토큰 생성
        String newAccessToken = jwtUtil.createJwt("access", email, role, 600000L); // 10분 유효 기간
        String newRefreshToken = jwtUtil.createJwt("refresh", email, role, 86400000L); // 1일 유효 기간

        refreshRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(email, newRefreshToken, 86400000L);

        // 토큰을 담은 Map 객체 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccessToken);
        tokens.put("refresh", newRefreshToken);

        return tokens;
    }


    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
