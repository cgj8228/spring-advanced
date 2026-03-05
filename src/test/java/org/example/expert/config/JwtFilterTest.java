// 목적: ExpiredJwtException 생성 시 jjwt 버전별 Header 빌더 차이로 컴파일 깨지는 문제 제거(항상 컴파일되게)
package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Test
    void authPath_bypassesFilter() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/auth/login");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        verify(chain).doFilter(req, res);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void missingAuthorizationHeader_returns401() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        assertThat(res.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(res.getContentAsString()).contains("인증이 필요합니다.");
        verifyNoInteractions(chain);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void claimsNull_returns401() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        req.addHeader("Authorization", "Bearer abc");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.substringToken("Bearer abc")).thenReturn("abc");
        when(jwtUtil.extractClaims("abc")).thenReturn(null);

        filter.doFilter(req, res, chain);

        assertThat(res.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(res.getContentAsString()).contains("인증이 필요합니다.");
        verifyNoInteractions(chain);
    }

    @Test
    void validToken_setsAttributes_andContinues() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        req.addHeader("Authorization", "Bearer ok");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        Claims claims = Jwts.claims().setSubject("123");
        claims.put("email", "a@a.com");
        claims.put("userRole", org.example.expert.domain.user.enums.UserRole.USER.name());

        when(jwtUtil.substringToken("Bearer ok")).thenReturn("ok");
        when(jwtUtil.extractClaims("ok")).thenReturn(claims);

        filter.doFilter(req, res, chain);

        assertThat(req.getAttribute("userId")).isEqualTo(123L);
        assertThat(req.getAttribute("email")).isEqualTo("a@a.com");
        assertThat(req.getAttribute("userRole")).isEqualTo(org.example.expert.domain.user.enums.UserRole.USER);
        verify(chain).doFilter(req, res);
    }

    @Test
    void expiredJwt_returns401() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        req.addHeader("Authorization", "Bearer exp");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.substringToken("Bearer exp")).thenReturn("exp");

        Claims claims = Jwts.claims().setSubject("9");
        claims.setExpiration(new Date(System.currentTimeMillis() - 1000));

        // ✅ jjwt 버전 차이(Header.builder/build 등) 안 타게 null로 생성
        ExpiredJwtException ex = new ExpiredJwtException(null, claims, "expired");
        when(jwtUtil.extractClaims("exp")).thenThrow(ex);

        filter.doFilter(req, res, chain);

        assertThat(res.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(res.getContentAsString()).contains("인증이 필요합니다.");
        verifyNoInteractions(chain);
    }

    @Test
    void malformedJwt_returns400() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        req.addHeader("Authorization", "Bearer bad");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.substringToken("Bearer bad")).thenReturn("bad");
        when(jwtUtil.extractClaims("bad")).thenThrow(new MalformedJwtException("bad"));

        filter.doFilter(req, res, chain);

        assertThat(res.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(res.getContentAsString()).contains("인증이 필요합니다.");
        verifyNoInteractions(chain);
    }

    @Test
    void unexpectedException_returns500() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtFilter filter = new JwtFilter(jwtUtil, new ObjectMapper());

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/todos");
        req.addHeader("Authorization", "Bearer err");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.substringToken("Bearer err")).thenReturn("err");
        when(jwtUtil.extractClaims("err")).thenThrow(new RuntimeException("boom"));

        filter.doFilter(req, res, chain);

        assertThat(res.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(res.getContentAsString()).contains("요청 처리 중 오류가 발생했습니다.");
        verifyNoInteractions(chain);
    }
}