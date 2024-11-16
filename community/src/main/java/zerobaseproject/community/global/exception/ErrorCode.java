package zerobaseproject.community.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

   ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다"),
   USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을수 없는 유저입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),

    INVALID_OR_EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 Refresh Token입니다."),
    TOKEN_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 삭제에 실패했습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Refresh Token이 존재하지 않습니다."),

    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 값입니다"),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "작성자만 글을 수정할 수 있습니다."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String description;
}
