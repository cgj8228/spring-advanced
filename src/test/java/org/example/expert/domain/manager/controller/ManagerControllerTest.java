// 목적: ManagerController 라인 커버리지(서비스 위임 + deleteManager 토큰 파싱) 100%용 단위 테스트
package org.example.expert.domain.manager.controller;

import io.jsonwebtoken.Claims;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ManagerControllerTest {

    @Test
    void saveManager_returnsOk_andDelegates() {
        ManagerService managerService = mock(ManagerService.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        ManagerController controller = new ManagerController(managerService, jwtUtil);

        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        ManagerSaveRequest req = new ManagerSaveRequest(2L);
        ManagerSaveResponse serviceRes = new ManagerSaveResponse(1L, null);

        when(managerService.saveManager(same(authUser), eq(10L), same(req))).thenReturn(serviceRes);

        ResponseEntity<ManagerSaveResponse> res = controller.saveManager(authUser, 10L, req);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(managerService).saveManager(same(authUser), eq(10L), same(req));
    }

    @Test
    void getMembers_returnsOk_andDelegates() {
        ManagerService managerService = mock(ManagerService.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        ManagerController controller = new ManagerController(managerService, jwtUtil);

        List<ManagerResponse> serviceRes = List.of(new ManagerResponse(1L, null));
        when(managerService.getManagers(10L)).thenReturn(serviceRes);

        ResponseEntity<List<ManagerResponse>> res = controller.getMembers(10L);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(managerService).getManagers(10L);
    }

    @Test
    void deleteManager_parsesBearerAndDelegates() {
        ManagerService managerService = mock(ManagerService.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        ManagerController controller = new ManagerController(managerService, jwtUtil);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("123");
        when(jwtUtil.extractClaims("token")).thenReturn(claims);

        controller.deleteManager("Bearer token", 10L, 99L);

        verify(jwtUtil).extractClaims("token");
        verify(managerService).deleteManager(123L, 10L, 99L);
    }
}