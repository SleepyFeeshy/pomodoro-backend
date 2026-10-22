package com.pomodoro.backend.auth.service;

import com.pomodoro.backend.auth.model.GoogleLoginResponse;
import com.pomodoro.backend.auth.model.User;
import com.pomodoro.backend.auth.repository.UserAuthCredentialRepository;
import com.pomodoro.backend.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GoogleAuthServiceUnitTests {

    @Mock private UserAuthCredentialRepository userAuthCredentialRepository;
    @Mock private UserRepository userRepository;
    @Mock private JwtDecoder jwtDecoder;

    private GoogleAuthService googleAuthService;

    @BeforeEach
    void setup() {
        this.googleAuthService = new GoogleAuthService(userAuthCredentialRepository, userRepository, jwtDecoder);
    }

    @Test
    @DisplayName("""
            GIVEN New user sign in request
            WHEN User signs in
            THEN sign in user and create user
            """
    )
    void newUserSignInCreatesUserFromGoogleLoginResponse() {
        // ARRANGE
        // Mock Google Response
        String email = "user@gmail.com";
        GoogleLoginResponse googleLoginResponse = new GoogleLoginResponse(
                email, "user", "picture", "googleId"
        );
        UUID generatedUserId = UUID.randomUUID();
        when(userRepository.upsertUser(eq(email))).thenReturn(generatedUserId);

        // ACT
        googleAuthService.signInOrCreateUser(googleLoginResponse);

        // ASSERT
        verify(userRepository)
                .upsertUser(eq(email));
        verify(userAuthCredentialRepository)
                .upsertUserAuthCredential(eq(generatedUserId), eq("google"), any(String.class), any(String.class), any(OffsetDateTime.class));
    }

//    @Test
//    @DisplayName("""
//            GIVEN Existing user sign in request
//            WHEN User signs in
//            THEN sign in user without creating a new user
//            """
//    )
//    void existingUserSignInFromGoogleLoginResponse() {
//        // ARRANGE
//        // Mock Google Response
//        String email = "user@gmail.com";
//        GoogleLoginResponse googleLoginResponse = new GoogleLoginResponse(
//                email, "user", "picture", "googleId"
//        );
//        UUID generatedUserId = UUID.randomUUID();
//        when(userRepository.upsertUser(eq(email))).thenReturn(generatedUserId);
//
//        // ACT
//        googleAuthService.signInOrCreateUser(googleLoginResponse);
//
//        // ASSERT
//        verify(userRepository)
//                .upsertUser(eq(email));
//        verify(userAuthCredentialRepository)
//                .upsertUserAuthCredential(eq(generatedUserId), eq("google"), any(String.class), any(String.class), any(OffsetDateTime.class));
//    }
}
