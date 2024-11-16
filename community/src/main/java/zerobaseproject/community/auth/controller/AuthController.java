package zerobaseproject.community.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.auth.dto.RegisterDTO;
import zerobaseproject.community.auth.dto.SignInDto;
import zerobaseproject.community.auth.dto.SignOutRequestDto;
import zerobaseproject.community.auth.service.AuthService;
import zerobaseproject.community.global.exception.SuccessCode;

/**
 * 인증 관련 요청을 처리하는 컨트롤러 클래스입니다.
 * 회원가입, 로그인, 로그아웃, 토큰 갱신과 관련된 기능을 제공합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param registerDTO 회원가입 정보가 포함된 DTO 객체
     * @return 회원가입 성공 메시지와 HTTP 상태 코드를 반환합니다.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid RegisterDTO registerDTO) {
        authService.signUp(registerDTO);
        return new ResponseEntity<>(SuccessCode.SIGN_UP_SUCCESS.getMessage(), SuccessCode.SIGN_UP_SUCCESS.getStatus());
    }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param request 로그인 정보가 포함된 DTO 객체
     * @return 성공 메시지와 함께 Authorization 헤더에 Access Token과 Refresh Token을 포함하여 반환합니다.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInDto.Request request) {
        SignInDto.Response tokens = authService.signIn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokens.getAccessToken());
        headers.set("Refresh-Token", "Bearer " + tokens.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.SIGN_IN_SUCCESS.getMessage());
    }

    /**
     * 로그아웃 요청을 처리합니다.
     *
     * @param accessToken       로그아웃할 사용자의 Access Token
     * @param signOutRequestDto 로그아웃 요청의 이메일이 포함된 DTO 객체
     * @return 로그아웃 성공 메시지와 HTTP 상태 코드를 반환합니다.
     */
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@RequestHeader("Authorization") String accessToken,
                                          @RequestBody @Valid SignOutRequestDto signOutRequestDto) {
        String email = signOutRequestDto.getEmail();
        String token = accessToken.replace("Bearer ", "");  // Bearer 제거
        authService.signOut(email, token);
        return new ResponseEntity<>(SuccessCode.SIGN_OUT_SUCCESS.getMessage(), SuccessCode.SIGN_OUT_SUCCESS.getStatus());
    }

    /**
     * Refresh Token을 사용하여 Access Token을 재발급합니다.
     *
     * @param refreshToken Refresh Token 헤더에서 가져온 토큰
     * @return 새로 발급된 Access Token과 함께 성공 메시지를 반환합니다.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");
        String newAccessToken = authService.reissueToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(SuccessCode.TOKEN_REFRESH_SUCCESS.getMessage());
    }
}
