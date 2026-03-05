// 목적: UserAdminService 라인 커버리지 100%(성공/유저없음/role 파싱 실패) 단위 테스트
package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAdminServiceTest {

    @Test
    void changeUserRole_success_updatesRole() {
        UserRepository repo = mock(UserRepository.class);
        UserAdminService service = new UserAdminService(repo);

        User user = spy(new User("a@a.com", "pw", UserRole.USER));
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        service.changeUserRole(1L, new UserRoleChangeRequest("ADMIN"));

        verify(user).updateRole(UserRole.ADMIN);
    }

    @Test
    void changeUserRole_userNotFound_throws() {
        UserRepository repo = mock(UserRepository.class);
        UserAdminService service = new UserAdminService(repo);

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeUserRole(1L, new UserRoleChangeRequest("ADMIN")))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("User not found");
    }

    @Test
    void changeUserRole_invalidRole_throws() {
        UserRepository repo = mock(UserRepository.class);
        UserAdminService service = new UserAdminService(repo);

        User user = new User("a@a.com", "pw", UserRole.USER);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.changeUserRole(1L, new UserRoleChangeRequest("NOPE")))
                .isInstanceOf(InvalidRequestException.class);
    }
}