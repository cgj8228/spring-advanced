// 목적: UserController 라인 커버리지(서비스 위임/응답) 100%용 단위 테스트
package org.example.expert.domain.user.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void getUser_returnsOk_andDelegates() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        UserResponse serviceRes = new UserResponse(1L, "a@a.com");
        when(userService.getUser(1L)).thenReturn(serviceRes);

        ResponseEntity<UserResponse> res = controller.getUser(1L);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(userService).getUser(1L);
    }

    @Test
    void changePassword_delegatesUsingAuthUserId() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        AuthUser authUser = new AuthUser(7L, "a@a.com", UserRole.USER);
        UserChangePasswordRequest req = new UserChangePasswordRequest("oldPw", "NewPass1");

        controller.changePassword(authUser, req);

        verify(userService).changePassword(7L, req);
    }
}