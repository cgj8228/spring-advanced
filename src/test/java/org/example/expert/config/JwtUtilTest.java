// 목적: JwtUtil 라인 커버리지 100%(init/createToken/substringToken/extractClaims) 단위 테스트
package org.example.expert.config;

import io.jsonwebtoken.Claims;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    @Test
    void init_createToken_extractClaims_success() {
        JwtUtil jwtUtil = new JwtUtil();

        // 256-bit(32바이트) 키를 base64로 세팅
        byte[] rawKey = "0123456789ABCDEF0123456789ABCDEF".getBytes();
        String b64 = Base64.getEncoder().encodeToString(rawKey);

        ReflectionTestUtils.setField(jwtUtil, "secretKey", b64);
        jwtUtil.init();

        String bearer = jwtUtil.createToken(1L, "a@a.com", UserRole.USER);
        assertThat(bearer).startsWith("Bearer ");

        String token = jwtUtil.substringToken(bearer);
        assertThat(token).isNotBlank();

        Claims claims = jwtUtil.extractClaims(token);
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("email", String.class)).isEqualTo("a@a.com");
        assertThat(claims.get("userRole", String.class)).isEqualTo(UserRole.USER.name());
    }

    @Test
    void substringToken_invalid_throws() {
        JwtUtil jwtUtil = new JwtUtil();
        assertThatThrownBy(() -> jwtUtil.substringToken("NoBearer xxx"))
                .isInstanceOf(RuntimeException.class);
    }
}