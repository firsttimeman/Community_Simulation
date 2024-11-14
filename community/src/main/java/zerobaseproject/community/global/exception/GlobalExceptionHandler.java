package zerobaseproject.community.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> validateError(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();

        List<String> collect = bindingResult.getFieldErrors()
                .stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(collect);
    }


    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> memberException(MemberException e) {
        return new ResponseEntity<>(e.getErrorCode().getDescription(), e.getErrorCode().getStatus());
    }



    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, String>> handleTokenException(TokenException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("code", ex.getErrorCode().name());
        response.put("message", ex.getErrorCode().getDescription());

        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleIllegalArgumentException(HttpMessageNotReadableException ex) {

            return new ResponseEntity<>(ErrorCode.INVALID_ENUM_VALUE.getDescription(),
                    ErrorCode.INVALID_ENUM_VALUE.getStatus());

    }
    // 그 외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
    }

}
