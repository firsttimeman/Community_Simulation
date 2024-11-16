package zerobaseproject.community.global.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{
    private final ErrorCode errorCode;

    public CommentException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
