package zerobaseproject.community.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
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

}
