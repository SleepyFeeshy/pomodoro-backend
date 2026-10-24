package com.pomodoro.backend.config;

import com.pomodoro.backend.auth.model.User;
import com.pomodoro.backend.auth.service.GoogleAuthService;
import com.pomodoro.backend.auth.service.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GoogleToAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private Logger logger = Logger.getLogger(GoogleToAuthorityConverter.class.getName());

    private final UserService userService;

    public GoogleToAuthorityConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract standard claims provided by Google
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        String picture = jwt.getClaimAsString("picture");
        String googleId = jwt.getSubject();

        // 1. Look up user in your database (or auto-register them if they are logging in for the first time)
//        UUID userId = (email, googleId, name);

        // 2. Fetch their roles/authorities from your database entity
        List<String> roles = userService.getUserRolesByEmail(email);
        logger.info(roles.toString());
        // 3. Map them into Spring Security's SimpleGrantedAuthority (Spring expects "ROLE_" prefix for hasRole())
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
