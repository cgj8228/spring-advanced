// 목적: Auth DTO/Exception 라인 커버리지(생성자/게터) 채우기
package org.example.expert.domain.auth;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthDtoCoverageTest {

    @Test
    void coverDtosAndException() {
        SigninRequest s1 = new SigninRequest();
        assertThat(s1.getEmail()).isNull();
        assertThat(s1.getPassword()).isNull();

        SigninRequest s2 = new SigninRequest("a@a.com", "pw");
        assertThat(s2.getEmail()).isEqualTo("a@a.com");
        assertThat(s2.getPassword()).isEqualTo("pw");

        SignupRequest u1 = new SignupRequest();
        assertThat(u1.getEmail()).isNull();
        assertThat(u1.getPassword()).isNull();
        assertThat(u1.getUserRole()).isNull();

        SignupRequest u2 = new SignupRequest("b@b.com", "pw2", "USER");
        assertThat(u2.getEmail()).isEqualTo("b@b.com");
        assertThat(u2.getPassword()).isEqualTo("pw2");
        assertThat(u2.getUserRole()).isEqualTo("USER");

        SignupResponse signupRes = new SignupResponse("Bearer x");
        assertThat(signupRes.getBearerToken()).isEqualTo("Bearer x");

        SigninResponse signinRes = new SigninResponse("Bearer y");
        assertThat(signinRes.getBearerToken()).isEqualTo("Bearer y");

        AuthException ex = new AuthException("bad");
        assertThat(ex.getMessage()).isEqualTo("bad");
    }
}