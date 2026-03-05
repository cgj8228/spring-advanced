// 목적: TodoController 라인 커버리지(서비스 위임/응답) 100%용 단위 테스트
package org.example.expert.domain.todo.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Test
    void saveTodo_returnsOk_andDelegatesToService() {
        TodoService todoService = mock(TodoService.class);
        TodoController controller = new TodoController(todoService);

        AuthUser authUser = mock(AuthUser.class);
        TodoSaveRequest req = new TodoSaveRequest("t", "c");
        TodoSaveResponse serviceRes = new TodoSaveResponse(1L, "t", "c", "SUNNY", null);

        when(todoService.saveTodo(same(authUser), same(req))).thenReturn(serviceRes);

        ResponseEntity<TodoSaveResponse> res = controller.saveTodo(authUser, req);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(todoService).saveTodo(same(authUser), same(req));
    }

    @Test
    void getTodos_returnsOk_andDelegatesToService() {
        TodoService todoService = mock(TodoService.class);
        TodoController controller = new TodoController(todoService);

        Page<TodoResponse> page = new PageImpl<>(
                List.of(new TodoResponse(1L, "t", "c", "SUNNY", null, LocalDateTime.now(), LocalDateTime.now()))
        );
        when(todoService.getTodos(1, 10)).thenReturn(page);

        ResponseEntity<Page<TodoResponse>> res = controller.getTodos(1, 10);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(page);
        verify(todoService).getTodos(1, 10);
    }

    @Test
    void getTodo_returnsOk_andDelegatesToService() {
        TodoService todoService = mock(TodoService.class);
        TodoController controller = new TodoController(todoService);

        TodoResponse serviceRes = new TodoResponse(7L, "t", "c", "SUNNY", null, LocalDateTime.now(), LocalDateTime.now());
        when(todoService.getTodo(7L)).thenReturn(serviceRes);

        ResponseEntity<TodoResponse> res = controller.getTodo(7L);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(todoService).getTodo(7L);
    }
}