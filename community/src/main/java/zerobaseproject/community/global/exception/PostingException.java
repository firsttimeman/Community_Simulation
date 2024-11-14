package zerobaseproject.community.global.exception;

import lombok.Getter;

@Getter
public class PostingException extends RuntimeException{
    private final ErrorCode errorCode;

    public PostingException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
