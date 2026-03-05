// 목적: AuthService 라인 커버리지 100%(signup/signin 성공/예외 분기) 단위 테스트
package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void signup_duplicateEmail_throwsInvalidRequestException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil);

        SignupRequest req = new SignupRequest("dup@a.com", "pw", "USER");
        when(userRepository.existsByEmail("dup@a.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signup(req))
                .isInstanceOf(InvalidRequestException.class);

        verify(userRepository).existsByEmail("dup@a.com");
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void signup_success_returnsBearerToken() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil);

        SignupRequest req = new SignupRequest("a@a.com", "pw", "USER");

        when(userRepository.existsByEmail("a@a.com")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("ENC");
        when(jwtUtil.createToken(any(), anyString(), any(UserRole.class))).thenReturn("Bearer token");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            setFieldIfExists(u, "id", 1L);
            return u;
        });

        SignupResponse res = authService.signup(req);

        assertThat(res.getBearerToken()).isEqualTo("Bearer token");
        verify(userRepository).existsByEmail("a@a.com");
        verify(passwordEncoder).encode("pw");
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).createToken(eq(1L), eq("a@a.com"), eq(UserRole.USER));
    }

    @Test
    void signin_userNotFound_throwsInvalidRequestException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil);

        when(userRepository.findByEmail("none@a.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.signin(new SigninRequest("none@a.com", "pw")))
                .isInstanceOf(InvalidRequestException.class);

        verify(userRepository).findByEmail("none@a.com");
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void signin_passwordMismatch_throwsAuthException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil);

        User user = mock(User.class);
        when(user.getPassword()).thenReturn("ENC");
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pw", "ENC")).thenReturn(false);

        assertThatThrownBy(() -> authService.signin(new SigninRequest("a@a.com", "pw")))
                .isInstanceOf(AuthException.class);

        verify(userRepository).findByEmail("a@a.com");
        verify(passwordEncoder).matches("pw", "ENC");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void signin_success_returnsBearerToken() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtUtil);

        User user = mock(User.class);
        when(user.getId()).thenReturn(7L);
        when(user.getEmail()).thenReturn("a@a.com");
        when(user.getPassword()).thenReturn("ENC");
        when(user.getUserRole()).thenReturn(UserRole.USER);

        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pw", "ENC")).thenReturn(true);
        when(jwtUtil.createToken(7L, "a@a.com", UserRole.USER)).thenReturn("Bearer token");

        SigninResponse res = authService.signin(new SigninRequest("a@a.com", "pw"));

        assertThat(res.getBearerToken()).isEqualTo("Bearer token");
        verify(jwtUtil).createToken(7L, "a@a.com", UserRole.USER);
    }

    private static void setFieldIfExists(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception ignored) {
        }
    }
}