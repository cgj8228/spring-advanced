// 목적: AuthController 라인 커버리지(서비스 위임/반환) 100%용 단위 테스트
package org.example.expert.domain.auth.controller;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Test
    void signup_delegatesToService() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        SignupRequest req = new SignupRequest("a@a.com", "pw", "USER");
        SignupResponse serviceRes = new SignupResponse("Bearer token");

        when(authService.signup(same(req))).thenReturn(serviceRes);

        SignupResponse res = controller.signup(req);

        assertThat(res).isSameAs(serviceRes);
        verify(authService).signup(same(req));
    }

    @Test
    void signin_delegatesToService() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        SigninRequest req = new SigninRequest("a@a.com", "pw");
        SigninResponse serviceRes = new SigninResponse("Bearer token");

        when(authService.signin(same(req))).thenReturn(serviceRes);

        SigninResponse res = controller.signin(req);

        assertThat(res).isSameAs(serviceRes);
        verify(authService).signin(same(req));
    }
}