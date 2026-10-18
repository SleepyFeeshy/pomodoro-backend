package com.pomodoro.backend.auth.service;

import com.pomodoro.backend.auth.model.GoogleLoginResponse;
import com.pomodoro.backend.auth.repository.UserAuthCredentialRepository;
import com.pomodoro.backend.auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class GoogleAuthService {
    private final UserAuthCredentialRepository userAuthCredentialRepository;
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    public GoogleAuthService(UserAuthCredentialRepository userAuthCredentialRepository, UserRepository userRepository, JwtDecoder jwtDecoder) {
        this.userAuthCredentialRepository = userAuthCredentialRepository;
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
    }

    public GoogleLoginResponse googleVerifyToken(String idToken) {
        Jwt jwt = jwtDecoder.decode(idToken);

        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        String picture = jwt.getClaimAsString("picture");
        String googleId = jwt.getSubject();

        return new GoogleLoginResponse(email, name, picture, googleId);
    }

    public void signInOrCreateUser(GoogleLoginResponse googleLoginResponse) {
        UUID user_id = userRepository.upsertUser(googleLoginResponse.getEmail());
        userAuthCredentialRepository.upsertUserAuthCredential(user_id, "google", googleLoginResponse.getGoogleId(), googleLoginResponse.getEmail(), OffsetDateTime.now());
    }
}
