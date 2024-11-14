package zerobaseproject.community.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";

    public void saveRefreshToken(String email, String refreshToken, long duration, TimeUnit timeUnit) {
        String key = REFRESH_TOKEN_PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, duration, timeUnit);
    }

    public void deleteRefreshToken(String email) {
        String key = REFRESH_TOKEN_PREFIX + email;
        redisTemplate.delete(key);
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + email);
    }

    public void addToBlacklist(String email, String accessToken, long duration, TimeUnit timeUnit) {
        String key = BLACKLIST_PREFIX + email + ":" + accessToken;
        redisTemplate.opsForValue().set(key, "logged-out", duration, timeUnit);
    }

    public boolean isTokenInBlacklist(String email, String accessToken) {
        String key = BLACKLIST_PREFIX + email + ":" + accessToken;
        return redisTemplate.hasKey(key);
    }
}
