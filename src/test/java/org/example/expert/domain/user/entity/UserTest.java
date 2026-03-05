// 목적: User DTO/Entity/Enum 라인 커버리지(생성자/게터/of/fromAuthUser/update 메서드) 채우기
package org.example.expert.domain.user.entity;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSaveResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void coverDtosEntityEnum() {
        UserChangePasswordRequest p0 = new UserChangePasswordRequest();
        assertThat(p0.getOldPassword()).isNull();
        assertThat(p0.getNewPassword()).isNull();

        UserChangePasswordRequest p1 = new UserChangePasswordRequest("old", "NewPass1");
        assertThat(p1.getNewPassword()).isEqualTo("NewPass1");

        UserRoleChangeRequest r0 = new UserRoleChangeRequest();
        assertThat(r0.getRole()).isNull();

        UserRoleChangeRequest r1 = new UserRoleChangeRequest("ADMIN");
        assertThat(r1.getRole()).isEqualTo("ADMIN");

        UserResponse u1 = new UserResponse(1L, "a@a.com");
        assertThat(u1.getEmail()).isEqualTo("a@a.com");

        UserSaveResponse u2 = new UserSaveResponse("Bearer x");
        assertThat(u2.getBearerToken()).isEqualTo("Bearer x");

        assertThat(UserRole.of("admin")).isEqualTo(UserRole.ADMIN);
        assertThatThrownBy(() -> UserRole.of("nope"))
                .isInstanceOf(InvalidRequestException.class);

        AuthUser authUser = new AuthUser(9L, "x@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        assertThat(user.getId()).isEqualTo(9L);
        assertThat(user.getEmail()).isEqualTo("x@a.com");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);

        user.changePassword("p2");
        assertThat(user.getPassword()).isEqualTo("p2");

        user.updateRole(UserRole.ADMIN);
        assertThat(user.getUserRole()).isEqualTo(UserRole.ADMIN);

        // id 라인 커버(게터 호출용)
        ReflectionTestUtils.setField(user, "id", 9L);
        assertThat(user.getId()).isEqualTo(9L);
    }
}