// 목적: UserService 라인 커버리지 100%(getUser/비번변경 성공 + 예외 3분기) 단위 테스트
package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void getUser_success_returnsDto() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("a@a.com");
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserResponse res = service.getUser(1L);

        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getEmail()).isEqualTo("a@a.com");
    }

    @Test
    void getUser_notFound_throws() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUser(1L))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("User not found");
    }

    @Test
    void changePassword_userNotFound_throws() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changePassword(1L, new UserChangePasswordRequest("old", "NewPass1")))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("User not found");
    }

    @Test
    void changePassword_newPasswordSameAsOld_throws() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        User user = new User("a@a.com", "ENC", UserRole.USER);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        // newPassword가 현재 비번과 matches -> 예외
        when(encoder.matches("NewPass1", "ENC")).thenReturn(true);

        assertThatThrownBy(() -> service.changePassword(1L, new UserChangePasswordRequest("old", "NewPass1")))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    }

    @Test
    void changePassword_oldPasswordMismatch_throws() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        User user = new User("a@a.com", "ENC", UserRole.USER);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        when(encoder.matches("NewPass1", "ENC")).thenReturn(false);
        when(encoder.matches("old", "ENC")).thenReturn(false);

        assertThatThrownBy(() -> service.changePassword(1L, new UserChangePasswordRequest("old", "NewPass1")))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("잘못된 비밀번호입니다.");
    }

    @Test
    void changePassword_success_encodesAndUpdates() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repo, encoder);

        User user = spy(new User("a@a.com", "ENC", UserRole.USER));
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        when(encoder.matches("NewPass1", "ENC")).thenReturn(false);
        when(encoder.matches("old", "ENC")).thenReturn(true);
        when(encoder.encode("NewPass1")).thenReturn("ENC2");

        service.changePassword(1L, new UserChangePasswordRequest("old", "NewPass1"));

        verify(user).changePassword("ENC2");
        verify(encoder).encode("NewPass1");
    }
}