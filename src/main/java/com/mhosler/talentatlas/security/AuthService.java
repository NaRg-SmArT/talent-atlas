package com.mhosler.TalentAtlas.security;

import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user;

        try {
            user = userService.findEntityByEmail(authRequest.getEmail());
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        try {
            String subject = jwtService.extractSubject(refreshToken);

            if (subject == null || !jwtService.isRefreshTokenValid(refreshToken, subject)) {
                throw new BadCredentialsException("Invalid refresh token");
            }

            String newAccessToken = jwtService.generateAccessToken(subject);
            return new AuthResponse(newAccessToken, refreshToken);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
