// 목적: CommentAdminService 라인 커버리지(삭제 위임) 100%용 단위 테스트
package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommentAdminServiceTest {

    @Test
    void deleteComment_callsRepositoryDeleteById() {
        CommentRepository repo = mock(CommentRepository.class);
        CommentAdminService service = new CommentAdminService(repo);

        service.deleteComment(5L);

        verify(repo).deleteById(5L);
    }
}