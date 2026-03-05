// 목적: ExpertApplication.main() 라인 커버리지 100%를 안전하게 채우기(실제 서버 부팅 없이 SpringApplication.run 가로채기)
package org.example.expert;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

class ExpertApplicationTest {

    @Test
    void main_callsSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(eq(ExpertApplication.class), any(String[].class)))
                    .thenReturn(null);

            ExpertApplication.main(new String[]{});

            mocked.verify(() -> SpringApplication.run(eq(ExpertApplication.class), any(String[].class)));
        }
    }
}