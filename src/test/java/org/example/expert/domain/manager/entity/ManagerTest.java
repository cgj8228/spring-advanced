// 목적: Manager DTO/Entity 생성자+게터 라인 커버리지 채우기
package org.example.expert.domain.manager.entity;

import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ManagerDtoEntityCoverageTest {

    @Test
    void coverDtosAndEntity() {
        ManagerSaveRequest r0 = new ManagerSaveRequest();
        assertThat(r0.getManagerUserId()).isNull();

        ManagerSaveRequest r1 = new ManagerSaveRequest(2L);
        assertThat(r1.getManagerUserId()).isEqualTo(2L);

        ManagerSaveResponse s1 = new ManagerSaveResponse(1L, null);
        assertThat(s1.getId()).isEqualTo(1L);

        ManagerResponse r2 = new ManagerResponse(3L, null);
        assertThat(r2.getId()).isEqualTo(3L);

        User u = new User("a@a.com", "pw", UserRole.USER);
        Todo t = new Todo();
        ReflectionTestUtils.setField(t, "id", 10L);

        Manager m = new Manager(u, t);
        ReflectionTestUtils.setField(m, "id", 7L);

        assertThat(m.getId()).isEqualTo(7L);
        assertThat(m.getUser()).isSameAs(u);
        assertThat(m.getTodo()).isSameAs(t);
    }
}