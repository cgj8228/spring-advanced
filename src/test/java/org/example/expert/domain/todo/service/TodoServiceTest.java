// 목적: TodoService 라인 커버리지 100%용(성공/예외/매핑) 단위 테스트
package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Test
    void saveTodo_success() {
        TodoRepository todoRepository = mock(TodoRepository.class);
        WeatherClient weatherClient = mock(WeatherClient.class);
        TodoService todoService = new TodoService(todoRepository, weatherClient);

        AuthUser authUser = newAuthUser(11L, "a@a.com", UserRole.USER);
        TodoSaveRequest req = new TodoSaveRequest("title", "contents");

        when(weatherClient.getTodayWeather()).thenReturn("SUNNY");

        ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
        when(todoRepository.save(todoCaptor.capture())).thenAnswer(inv -> {
            Todo t = inv.getArgument(0);
            // id 세팅(엔티티 id는 보통 persist 후 채워지지만, 테스트에서는 리플렉션으로 채움)
            setField(t, "id", 100L);
            return t;
        });

        TodoSaveResponse res = todoService.saveTodo(authUser, req);

        assertThat(res.getId()).isEqualTo(100L);
        assertThat(res.getTitle()).isEqualTo("title");
        assertThat(res.getContents()).isEqualTo("contents");
        assertThat(res.getWeather()).isEqualTo("SUNNY");
        assertThat(res.getUser()).isNotNull();

        Todo saved = todoCaptor.getValue();
        assertThat(saved.getTitle()).isEqualTo("title");
        assertThat(saved.getContents()).isEqualTo("contents");
        assertThat(saved.getWeather()).isEqualTo("SUNNY");
        assertThat(saved.getUser()).isNotNull();

        verify(weatherClient).getTodayWeather();
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void getTodos_success_mapsPage() {
        TodoRepository todoRepository = mock(TodoRepository.class);
        WeatherClient weatherClient = mock(WeatherClient.class);
        TodoService todoService = new TodoService(todoRepository, weatherClient);

        Todo todo1 = mock(Todo.class);
        User user1 = mock(User.class);
        when(todo1.getId()).thenReturn(1L);
        when(todo1.getTitle()).thenReturn("t1");
        when(todo1.getContents()).thenReturn("c1");
        when(todo1.getWeather()).thenReturn("SUNNY");
        when(todo1.getUser()).thenReturn(user1);
        when(user1.getId()).thenReturn(10L);
        when(user1.getEmail()).thenReturn("u1@a.com");
        when(todo1.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));
        when(todo1.getModifiedAt()).thenReturn(LocalDateTime.now());

        Page<Todo> page = new PageImpl<>(List.of(todo1));

        when(todoRepository.findAllByOrderByModifiedAtDesc(any(Pageable.class))).thenReturn(page);

        Page<TodoResponse> res = todoService.getTodos(1, 10);

        assertThat(res.getTotalElements()).isEqualTo(1);
        TodoResponse dto = res.getContent().get(0);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("t1");
        assertThat(dto.getContents()).isEqualTo("c1");
        assertThat(dto.getWeather()).isEqualTo("SUNNY");
        assertThat(dto.getUser()).isNotNull();

        verify(todoRepository).findAllByOrderByModifiedAtDesc(any(Pageable.class));
    }

    @Test
    void getTodo_success() {
        TodoRepository todoRepository = mock(TodoRepository.class);
        WeatherClient weatherClient = mock(WeatherClient.class);
        TodoService todoService = new TodoService(todoRepository, weatherClient);

        Todo todo = mock(Todo.class);
        User user = mock(User.class);

        when(todoRepository.findByIdWithUser(7L)).thenReturn(Optional.of(todo));
        when(todo.getId()).thenReturn(7L);
        when(todo.getTitle()).thenReturn("t");
        when(todo.getContents()).thenReturn("c");
        when(todo.getWeather()).thenReturn("SUNNY");
        when(todo.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(99L);
        when(user.getEmail()).thenReturn("x@a.com");
        when(todo.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(2));
        when(todo.getModifiedAt()).thenReturn(LocalDateTime.now());

        TodoResponse res = todoService.getTodo(7L);

        assertThat(res.getId()).isEqualTo(7L);
        assertThat(res.getUser()).isNotNull();
        verify(todoRepository).findByIdWithUser(7L);
    }

    @Test
    void getTodo_notFound_throwsInvalidRequestException() {
        TodoRepository todoRepository = mock(TodoRepository.class);
        WeatherClient weatherClient = mock(WeatherClient.class);
        TodoService todoService = new TodoService(todoRepository, weatherClient);

        when(todoRepository.findByIdWithUser(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getTodo(404L))
                .isInstanceOf(InvalidRequestException.class);

        verify(todoRepository).findByIdWithUser(404L);
    }

    private static AuthUser newAuthUser(Long id, String email, UserRole role) {
        try {
            // 1) (Long, String, UserRole) 시도
            for (Constructor<?> c : AuthUser.class.getDeclaredConstructors()) {
                Class<?>[] p = c.getParameterTypes();
                if (p.length == 3 && p[0] == Long.class && p[1] == String.class && p[2] == UserRole.class) {
                    c.setAccessible(true);
                    return (AuthUser) c.newInstance(id, email, role);
                }
                if (p.length == 3 && (p[0] == long.class || p[0] == Long.class) && p[1] == String.class && p[2] == UserRole.class) {
                    c.setAccessible(true);
                    return (AuthUser) c.newInstance(id, email, role);
                }
            }

            // 2) (Long, String) 시도
            for (Constructor<?> c : AuthUser.class.getDeclaredConstructors()) {
                Class<?>[] p = c.getParameterTypes();
                if (p.length == 2 && (p[0] == long.class || p[0] == Long.class) && p[1] == String.class) {
                    c.setAccessible(true);
                    return (AuthUser) c.newInstance(id, email);
                }
            }

            // 3) no-args + 리플렉션 세팅
            Constructor<AuthUser> noArgs = AuthUser.class.getDeclaredConstructor();
            noArgs.setAccessible(true);
            AuthUser au = noArgs.newInstance();
            setField(au, "id", id);
            setField(au, "email", email);
            setField(au, "userRole", role);
            return au;
        } catch (Exception e) {
            throw new IllegalStateException("AuthUser 생성 실패: 생성자/필드 구조 확인 필요", e);
        }
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception ignored) {
        }
    }

    @Test
    void update_setsTitleAndContents() throws Exception {
        Constructor<Todo> ctor = Todo.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        Todo todo = ctor.newInstance();

        todo.update("newTitle", "newContents");

        assertThat(todo.getTitle()).isEqualTo("newTitle");
        assertThat(todo.getContents()).isEqualTo("newContents");
    }
}