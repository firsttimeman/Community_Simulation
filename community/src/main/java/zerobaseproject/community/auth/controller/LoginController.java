package zerobaseproject.community.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobaseproject.community.auth.dto.LoginDTO;
import zerobaseproject.community.auth.service.AuthService;
import zerobaseproject.community.security.jwt.CustomLogoutHandler;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    private final CustomLogoutHandler customLogoutHandler;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        authService.authenticateUser(loginDTO);


        return new ResponseEntity<>(loginDTO, HttpStatus.OK);
    }

    @PostMapping("/service-logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("POST /logout 요청이 들어왔습니다.");
        try {
            customLogoutHandler.logout(request, response);
            return ResponseEntity.ok("Logout successful");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }


}
