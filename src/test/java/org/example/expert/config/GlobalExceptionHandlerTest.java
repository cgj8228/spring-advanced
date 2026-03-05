// 목적: GlobalExceptionHandler 3개 핸들러 + 공통 응답(Map 구성) 라인 커버리지 100%
package org.example.expert.config;

import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void invalidRequest_returns400() {
        GlobalExceptionHandler geh = new GlobalExceptionHandler();

        ResponseEntity<Map<String, Object>> res =
                geh.invalidRequestExceptionException(new InvalidRequestException("bad"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().get("status")).isEqualTo("BAD_REQUEST");
        assertThat(res.getBody().get("code")).isEqualTo(400);
        assertThat(res.getBody().get("message")).isEqualTo("bad");
    }

    @Test
    void authException_returns401() {
        GlobalExceptionHandler geh = new GlobalExceptionHandler();

        ResponseEntity<Map<String, Object>> res =
                geh.handleAuthException(new AuthException("no"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().get("status")).isEqualTo("UNAUTHORIZED");
        assertThat(res.getBody().get("code")).isEqualTo(401);
        assertThat(res.getBody().get("message")).isEqualTo("no");
    }

    @Test
    void serverException_returns500() {
        GlobalExceptionHandler geh = new GlobalExceptionHandler();

        ResponseEntity<Map<String, Object>> res =
                geh.handleServerException(new ServerException("err"));

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().get("status")).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(res.getBody().get("code")).isEqualTo(500);
        assertThat(res.getBody().get("message")).isEqualTo("err");
    }
}