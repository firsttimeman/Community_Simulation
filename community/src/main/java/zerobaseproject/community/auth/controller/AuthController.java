package zerobaseproject.community.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.auth.dto.RegisterDTO;
import zerobaseproject.community.auth.dto.SignInDto;
import zerobaseproject.community.auth.service.AuthService;
import zerobaseproject.community.global.exception.SuccessCode;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterDTO registerDTO) {
        authService.signUp(registerDTO);
        return new ResponseEntity<>(SuccessCode.SIGN_UP_SUCCESS.getMessage(), SuccessCode.SIGN_UP_SUCCESS.getStatus());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInDto.Request request) {
        SignInDto.Response tokens = authService.signIn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokens.getAccessToken());
        headers.set("Refresh-Token", "Bearer " + tokens.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.SIGN_IN_SUCCESS.getMessage());
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@RequestHeader("Authorization") String accessToken,
                                          @RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String token = accessToken.replace("Bearer ", "");  // Bearer 제거
        authService.signOut(email, token);
        return new ResponseEntity<>(SuccessCode.SIGN_OUT_SUCCESS.getMessage(), SuccessCode.SIGN_OUT_SUCCESS.getStatus());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");
        String newAccessToken = authService.reissueToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok().headers(headers).body(SuccessCode.TOKEN_REFRESH_SUCCESS.getMessage());
    }
}
