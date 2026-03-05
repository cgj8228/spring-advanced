// 목적: Timestamped(abstract) 라인 커버리지 100% 채우기(게터 실행 + 필드값 리플렉션 세팅)
package org.example.expert.domain.common.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimestampedTest {

    static class TestTimestamped extends Timestamped {
    }

    @Test
    void cover_getters() throws Exception {
        TestTimestamped t = new TestTimestamped();

        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime modified = LocalDateTime.now();

        setField(Timestamped.class, t, "createdAt", created);
        setField(Timestamped.class, t, "modifiedAt", modified);

        assertThat(t.getCreatedAt()).isEqualTo(created);
        assertThat(t.getModifiedAt()).isEqualTo(modified);
    }

    private static void setField(Class<?> owner, Object target, String name, Object value) throws Exception {
        Field f = owner.getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}