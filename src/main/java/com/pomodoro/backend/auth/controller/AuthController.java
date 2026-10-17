package com.pomodoro.backend.auth.controller;

import com.pomodoro.backend.auth.model.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
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

    public AuthController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
        @RequestBody Map<String, String> requestBody
        )
    {
        String idToken = requestBody.get("token");
        // Verify token
        try {
//            Jwt jwt = jwtDecoder.decode(idToken);
//            logger.info(jwt.toString());
            Jwt jwt = jwtDecoder.decode(idToken);

            String email = jwt.getClaimAsString("email");
            String name = jwt.getClaimAsString("name");
            String picture = jwt.getClaimAsString("picture");
            String googleId = jwt.getSubject();

            logger.info(email);
            return ResponseEntity.status(200).body("Authentication successful.");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }

    }
}

