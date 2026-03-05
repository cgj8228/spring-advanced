package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private ManagerService managerService;

    @Test
    public void manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다() {
        // given
        long todoId = 1L;
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> managerService.getManagers(todoId));
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    void todo의_user가_null인_경우_예외가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        long todoId = 1L;
        long managerUserId = 2L;

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "user", null);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
            managerService.saveManager(authUser, todoId, managerSaveRequest)
        );

        assertEquals("일정을 생성한 유저만 담당자를 지정할 수 있습니다.", exception.getMessage());
    }

    @Test // 테스트코드 샘플
    public void manager_목록_조회에_성공한다() {
        // given
        long todoId = 1L;
        User user = new User("user1@example.com", "password", UserRole.USER);
        Todo todo = new Todo("Title", "Contents", "Sunny", user);
        ReflectionTestUtils.setField(todo, "id", todoId);

        Manager mockManager = new Manager(todo.getUser(), todo);
        List<Manager> managerList = List.of(mockManager);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(managerRepository.findByTodoIdWithUser(todoId)).willReturn(managerList);

        // when
        List<ManagerResponse> managerResponses = managerService.getManagers(todoId);

        // then
        assertEquals(1, managerResponses.size());
        assertEquals(mockManager.getId(), managerResponses.get(0).getId());
        assertEquals(mockManager.getUser().getEmail(), managerResponses.get(0).getUser().getEmail());
    }

    @Test // 테스트코드 샘플
    void todo가_정상적으로_등록된다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);  // 일정을 만든 유저

        long todoId = 1L;
        Todo todo = new Todo("Test Title", "Test Contents", "Sunny", user);

        long managerUserId = 2L;
        User managerUser = new User("b@b.com", "password", UserRole.USER);  // 매니저로 등록할 유저
        ReflectionTestUtils.setField(managerUser, "id", managerUserId);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId); // request dto 생성

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(userRepository.findById(managerUserId)).willReturn(Optional.of(managerUser));
        given(managerRepository.save(any(Manager.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ManagerSaveResponse response = managerService.saveManager(authUser, todoId, managerSaveRequest);

        // then
        assertNotNull(response);
        assertEquals(managerUser.getId(), response.getUser().getId());
        assertEquals(managerUser.getEmail(), response.getUser().getEmail());
    }

    // 추가
    @Test
    void manager_등록_중_Todo가_없으면_예외가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        long todoId = 999L;
        ManagerSaveRequest req = new ManagerSaveRequest(2L);

        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, todoId, req));
        assertEquals("Todo not found", ex.getMessage());
    }

    @Test
    void manager_등록_중_담당자_유저가_없으면_예외가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User creator = User.fromAuthUser(authUser);
        ReflectionTestUtils.setField(creator, "id", 1L);

        long todoId = 1L;
        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", creator);

        long managerUserId = 2L;
        ManagerSaveRequest req = new ManagerSaveRequest(managerUserId);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(userRepository.findById(managerUserId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, todoId, req));
        assertEquals("등록하려고 하는 담당자 유저가 존재하지 않습니다.", ex.getMessage());
    }

    @Test
    void manager_등록_중_일정작성자가_본인을_담당자로_등록하면_예외가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User creator = User.fromAuthUser(authUser);
        ReflectionTestUtils.setField(creator, "id", 1L);

        long todoId = 1L;
        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", creator);

        long managerUserId = 1L;
        User managerUser = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(managerUser, "id", managerUserId);

        ManagerSaveRequest req = new ManagerSaveRequest(managerUserId);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(userRepository.findById(managerUserId)).willReturn(Optional.of(managerUser));

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, todoId, req));
        assertEquals("일정 작성자는 본인을 담당자로 등록할 수 없습니다.", ex.getMessage());
    }

    @Test
    void manager_목록이_없으면_빈리스트를_반환한다() {
        // given
        long todoId = 1L;
        User user = new User("user1@example.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", user);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(managerRepository.findByTodoIdWithUser(todoId)).willReturn(List.of());

        // when
        List<ManagerResponse> result = managerService.getManagers(todoId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void manager_삭제_시_user가_없으면_예외가_발생한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(userId, todoId, managerId));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void manager_삭제_시_todo가_없으면_예외가_발생한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        User user = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(userId, todoId, managerId));
        assertEquals("Todo not found", ex.getMessage());
    }

    @Test
    void manager_삭제_시_일정작성자_불일치면_예외가_발생한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        User user = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        User other = new User("b@b.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(other, "id", 2L);

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", other);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(userId, todoId, managerId));
        assertEquals("해당 일정을 만든 유저가 유효하지 않습니다.", ex.getMessage());
    }

    @Test
    void manager_삭제_시_manager가_없으면_예외가_발생한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        User user = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", user);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(managerRepository.findById(managerId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(userId, todoId, managerId));
        assertEquals("Manager not found", ex.getMessage());
    }

    @Test
    void manager_삭제_시_다른Todo의_담당자면_예외가_발생한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        User creator = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(creator, "id", userId);

        Todo todo1 = new Todo();
        ReflectionTestUtils.setField(todo1, "id", todoId);
        ReflectionTestUtils.setField(todo1, "user", creator);

        Todo todo2 = new Todo();
        ReflectionTestUtils.setField(todo2, "id", 2L);
        ReflectionTestUtils.setField(todo2, "user", creator);

        User managerUser = new User("m@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(managerUser, "id", 9L);

        Manager manager = new Manager(managerUser, todo2);
        ReflectionTestUtils.setField(manager, "id", managerId);

        given(userRepository.findById(userId)).willReturn(Optional.of(creator));
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo1));
        given(managerRepository.findById(managerId)).willReturn(Optional.of(manager));

        // when & then
        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(userId, todoId, managerId));
        assertEquals("해당 일정에 등록된 담당자가 아닙니다.", ex.getMessage());
    }

    @Test
    void manager_삭제에_성공한다() {
        // given
        long userId = 1L;
        long todoId = 1L;
        long managerId = 1L;

        User creator = new User("a@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(creator, "id", userId);

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", todoId);
        ReflectionTestUtils.setField(todo, "user", creator);

        User managerUser = new User("m@a.com", "pw", UserRole.USER);
        ReflectionTestUtils.setField(managerUser, "id", 9L);

        Manager manager = new Manager(managerUser, todo);
        ReflectionTestUtils.setField(manager, "id", managerId);

        given(userRepository.findById(userId)).willReturn(Optional.of(creator));
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(managerRepository.findById(managerId)).willReturn(Optional.of(manager));

        // when
        managerService.deleteManager(userId, todoId, managerId);

        // then
        org.mockito.Mockito.verify(managerRepository).delete(manager);
    }
}
