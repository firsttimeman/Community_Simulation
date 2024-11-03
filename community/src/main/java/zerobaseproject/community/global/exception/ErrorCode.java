package zerobaseproject.community.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

   ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다");



    private final HttpStatus status;
    private final String description;
}
