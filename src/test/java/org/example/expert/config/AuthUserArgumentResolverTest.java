// 목적: AuthUserArgumentResolver supportsParameter 분기(정상/예외) + resolveArgument 라인 커버리지 100%
package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

class AuthUserArgumentResolverTest {

    static class DummyController {
        public void ok(@Auth AuthUser authUser) {}
        public void bad1(@Auth String notAuthUser) {}
        public void bad2(AuthUser authUser) {}
    }

    @Test
    void supportsParameter_ok_returnsTrue() throws Exception {
        AuthUserArgumentResolver r = new AuthUserArgumentResolver();
        Method m = DummyController.class.getDeclaredMethod("ok", AuthUser.class);
        MethodParameter p = new MethodParameter(m, 0);

        assertThat(r.supportsParameter(p)).isTrue();
    }

    @Test
    void supportsParameter_authButWrongType_throws() throws Exception {
        AuthUserArgumentResolver r = new AuthUserArgumentResolver();
        Method m = DummyController.class.getDeclaredMethod("bad1", String.class);
        MethodParameter p = new MethodParameter(m, 0);

        assertThatThrownBy(() -> r.supportsParameter(p))
                .isInstanceOf(AuthException.class)
                .hasMessage("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
    }

    @Test
    void supportsParameter_authUserButNoAuth_throws() throws Exception {
        AuthUserArgumentResolver r = new AuthUserArgumentResolver();
        Method m = DummyController.class.getDeclaredMethod("bad2", AuthUser.class);
        MethodParameter p = new MethodParameter(m, 0);

        assertThatThrownBy(() -> r.supportsParameter(p))
                .isInstanceOf(AuthException.class)
                .hasMessage("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
    }

    @Test
    void resolveArgument_buildsAuthUserFromRequestAttributes() throws Exception {
        AuthUserArgumentResolver r = new AuthUserArgumentResolver();
        Method m = DummyController.class.getDeclaredMethod("ok", AuthUser.class);
        MethodParameter p = new MethodParameter(m, 0);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setAttribute("userId", 1L);
        req.setAttribute("email", "a@a.com");
        req.setAttribute("userRole", "USER"); // resolver는 String으로 캐스팅함

        NativeWebRequest webRequest = new ServletWebRequest(req);

        Object resolved = r.resolveArgument(p, null, webRequest, null);

        assertThat(resolved).isInstanceOf(AuthUser.class);
        AuthUser au = (AuthUser) resolved;
        assertThat(au.getId()).isEqualTo(1L);
        assertThat(au.getEmail()).isEqualTo("a@a.com");
        assertThat(au.getUserRole()).isEqualTo(UserRole.USER);
    }
}