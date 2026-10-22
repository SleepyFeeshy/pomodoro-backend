package com.pomodoro.backend.auth.controller;

import com.pomodoro.backend.auth.model.GoogleLoginResponse;
import com.pomodoro.backend.auth.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger logger = Logger.getLogger(AuthController.class.getName());
    private final JwtDecoder jwtDecoder;
    private final GoogleAuthService googleAuthService;

    public AuthController(
            JwtDecoder jwtDecoder,
            GoogleAuthService googleAuthService

    ) {
        this.jwtDecoder = jwtDecoder;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
        @RequestBody Map<String, String> requestBody
        )
    {
        String idToken = requestBody.get("token");
        logger.info(idToken);
        // Verify token
        try {
            GoogleLoginResponse response = googleAuthService.googleVerifyToken(idToken);
            googleAuthService.signInOrCreateUser(response);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }

    }
}

