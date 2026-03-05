// 목적: Todo 엔티티(update) + DTO 생성자 라인 커버리지 채우기
package org.example.expert.domain.todo;

import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TodoSimpleCoverageTest {

    @Test
    void dtoAndEntityLines_cover() throws Exception {
        TodoSaveRequest req = new TodoSaveRequest("t", "c");
        assertThat(req.getTitle()).isEqualTo("t");
        assertThat(req.getContents()).isEqualTo("c");

        TodoSaveResponse saveRes = new TodoSaveResponse(1L, "t", "c", "SUNNY", null);
        assertThat(saveRes.getId()).isEqualTo(1L);

        TodoResponse res = new TodoResponse(2L, "t2", "c2", "CLOUDY", null, LocalDateTime.now(), LocalDateTime.now());
        assertThat(res.getId()).isEqualTo(2L);

        Constructor<Todo> noArgs = Todo.class.getDeclaredConstructor();
        noArgs.setAccessible(true);
        Todo todo = noArgs.newInstance();

        setField(todo, "title", "old");
        setField(todo, "contents", "oldc");

        todo.update("new", "newc");
        assertThat(todo.getTitle()).isEqualTo("new");
        assertThat(todo.getContents()).isEqualTo("newc");
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        var f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}