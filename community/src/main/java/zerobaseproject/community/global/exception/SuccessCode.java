package zerobaseproject.community.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    SIGN_UP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    SIGN_IN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    SIGN_OUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "Access Token이 재발급되었습니다.");

    private final HttpStatus status;
    private final String message;
}
