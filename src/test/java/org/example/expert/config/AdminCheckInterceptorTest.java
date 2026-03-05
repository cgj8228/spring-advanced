// 목적: AdminCheckInterceptor preHandle 분기(role null/USER/ADMIN) 라인 커버리지 100%
package org.example.expert.config;

import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AdminCheckInterceptorTest {

    @Test
    void preHandle_roleNull_returnsFalse_401() throws Exception {
        AdminCheckInterceptor interceptor = new AdminCheckInterceptor();

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/admin/test");
        MockHttpServletResponse res = new MockHttpServletResponse();

        boolean ok = interceptor.preHandle(req, res, new Object());

        assertThat(ok).isFalse();
        assertThat(res.getStatus()).isEqualTo(401);
    }

    @Test
    void preHandle_roleUser_returnsFalse_401() throws Exception {
        AdminCheckInterceptor interceptor = new AdminCheckInterceptor();

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/admin/test");
        req.setAttribute("userRole", UserRole.USER);
        MockHttpServletResponse res = new MockHttpServletResponse();

        boolean ok = interceptor.preHandle(req, res, new Object());

        assertThat(ok).isFalse();
        assertThat(res.getStatus()).isEqualTo(401);
    }

    @Test
    void preHandle_roleAdmin_returnsTrue() throws Exception {
        AdminCheckInterceptor interceptor = new AdminCheckInterceptor();

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/admin/test");
        req.setAttribute("userRole", UserRole.ADMIN);
        MockHttpServletResponse res = new MockHttpServletResponse();

        boolean ok = interceptor.preHandle(req, res, new Object());

        assertThat(ok).isTrue();
    }
}