// 목적: CommentAdminController 라인 커버리지(서비스 위임) 100%용 단위 테스트
package org.example.expert.domain.comment.controller;

import org.example.expert.domain.comment.service.CommentAdminService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommentAdminControllerTest {

    @Test
    void deleteComment_delegatesToService() {
        CommentAdminService service = mock(CommentAdminService.class);
        CommentAdminController controller = new CommentAdminController(service);

        controller.deleteComment(10L);

        verify(service).deleteComment(10L);
    }
}