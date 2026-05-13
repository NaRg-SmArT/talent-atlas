package com.mhosler.TalentAtlas.security;

import com.mhosler.TalentAtlas.user.User;
import com.mhosler.TalentAtlas.user.UserService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() {
        AuthRequest request = new AuthRequest("test@example.com", "password123");
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        when(userService.findEntityByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateAccessToken("test@example.com")).thenReturn("access-token");
        when(jwtService.generateRefreshToken("test@example.com")).thenReturn("refresh-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(userService).findEntityByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "hashedPassword");
        verify(jwtService).generateAccessToken("test@example.com");
        verify(jwtService).generateRefreshToken("test@example.com");
    }

    @Test
    void loginShouldThrowBadCredentialsWhenUserIsNotFound() {
        AuthRequest request = new AuthRequest("missing@example.com", "password123");

        when(userService.findEntityByEmail("missing@example.com"))
                .thenThrow(new RuntimeException("User not found"));

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userService).findEntityByEmail("missing@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateAccessToken(anyString());
        verify(jwtService, never()).generateRefreshToken(anyString());
    }

    @Test
    void loginShouldThrowBadCredentialsWhenPasswordDoesNotMatch() {
        AuthRequest request = new AuthRequest("test@example.com", "wrongPassword");
        User user = new User("Test User", "test@example.com", "hashedPassword", LocalDateTime.now());

        when(userService.findEntityByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userService).findEntityByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
        verify(jwtService, never()).generateAccessToken(anyString());
        verify(jwtService, never()).generateRefreshToken(anyString());
    }

    @Test
    void refreshShouldReturnNewAccessTokenWhenRefreshTokenIsValid() {
        String refreshToken = "valid-refresh-token";

        when(jwtService.extractSubject(refreshToken)).thenReturn("test@example.com");
        when(jwtService.isRefreshTokenValid(refreshToken, "test@example.com")).thenReturn(true);
        when(jwtService.generateAccessToken("test@example.com")).thenReturn("new-access-token");

        AuthResponse response = authService.refresh(refreshToken);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        verify(jwtService).extractSubject(refreshToken);
        verify(jwtService).isRefreshTokenValid(refreshToken, "test@example.com");
        verify(jwtService).generateAccessToken("test@example.com");
    }

    @Test
    void refreshShouldThrowBadCredentialsWhenSubjectIsNull() {
        String refreshToken = "invalid-refresh-token";

        when(jwtService.extractSubject(refreshToken)).thenReturn(null);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.refresh(refreshToken)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(jwtService).extractSubject(refreshToken);
        verify(jwtService, never()).generateAccessToken(anyString());
    }

    @Test
    void refreshShouldThrowBadCredentialsWhenTokenIsNotValid() {
        String refreshToken = "invalid-refresh-token";

        when(jwtService.extractSubject(refreshToken)).thenReturn("test@example.com");
        when(jwtService.isRefreshTokenValid(refreshToken, "test@example.com")).thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.refresh(refreshToken)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(jwtService).extractSubject(refreshToken);
        verify(jwtService).isRefreshTokenValid(refreshToken, "test@example.com");
        verify(jwtService, never()).generateAccessToken(anyString());
    }

    @Test
    void refreshShouldThrowBadCredentialsWhenJwtExceptionOccurs() {
        String refreshToken = "bad-token";

        when(jwtService.extractSubject(refreshToken)).thenThrow(new JwtException("Token parsing failed"));

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.refresh(refreshToken)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(jwtService).extractSubject(refreshToken);
        verify(jwtService, never()).generateAccessToken(anyString());
    }
}

