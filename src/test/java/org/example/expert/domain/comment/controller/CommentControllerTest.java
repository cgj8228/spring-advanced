// 목적: CommentController 라인 커버리지(서비스 위임/응답) 100%용 단위 테스트
package org.example.expert.domain.comment.controller;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Test
    void saveComment_returnsOk_andDelegates() {
        CommentService service = mock(CommentService.class);
        CommentController controller = new CommentController(service);

        AuthUser authUser = mock(AuthUser.class);
        CommentSaveRequest req = new CommentSaveRequest("hi");
        CommentSaveResponse serviceRes = new CommentSaveResponse(1L, "hi", null);

        when(service.saveComment(same(authUser), eq(7L), same(req))).thenReturn(serviceRes);

        ResponseEntity<CommentSaveResponse> res = controller.saveComment(authUser, 7L, req);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(serviceRes);
        verify(service).saveComment(same(authUser), eq(7L), same(req));
    }

    @Test
    void getComments_returnsOk_andDelegates() {
        CommentService service = mock(CommentService.class);
        CommentController controller = new CommentController(service);

        List<CommentResponse> list = List.of(new CommentResponse(1L, "c", null));
        when(service.getComments(7L)).thenReturn(list);

        ResponseEntity<List<CommentResponse>> res = controller.getComments(7L);

        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).isSameAs(list);
        verify(service).getComments(7L);
    }
}