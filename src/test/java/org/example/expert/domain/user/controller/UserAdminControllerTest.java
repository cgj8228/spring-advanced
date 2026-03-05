// 목적: UserAdminController 라인 커버리지(서비스 위임) 100%용 단위 테스트
package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UserAdminControllerTest {

    @Test
    void changeUserRole_delegatesToService() {
        UserAdminService service = mock(UserAdminService.class);
        UserAdminController controller = new UserAdminController(service);

        UserRoleChangeRequest req = new UserRoleChangeRequest("ADMIN");

        controller.changeUserRole(1L, req);

        verify(service).changeUserRole(1L, req);
    }
}