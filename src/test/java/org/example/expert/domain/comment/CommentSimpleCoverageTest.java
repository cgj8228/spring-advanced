// 목적: Comment 엔티티(update) + DTO 생성자/게터 라인 커버리지 채우기
package org.example.expert.domain.comment;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class CommentSimpleCoverageTest {

    @Test
    void coverDtosAndEntityUpdate() throws Exception {
        CommentSaveRequest r0 = new CommentSaveRequest();
        assertThat(r0.getContents()).isNull();

        CommentSaveRequest r1 = new CommentSaveRequest("c");
        assertThat(r1.getContents()).isEqualTo("c");

        CommentSaveResponse s1 = new CommentSaveResponse(1L, "c", null);
        assertThat(s1.getId()).isEqualTo(1L);

        CommentResponse r2 = new CommentResponse(2L, "cc", null);
        assertThat(r2.getId()).isEqualTo(2L);

        Constructor<Comment> ctor = Comment.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        Comment c = ctor.newInstance();

        c.update("new");
        assertThat(c.getContents()).isEqualTo("new");
    }
}