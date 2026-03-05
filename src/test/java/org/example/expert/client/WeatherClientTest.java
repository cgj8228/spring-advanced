// 목적: WeatherClient getTodayWeather 모든 분기(성공/상태코드/빈바디/오늘없음) 라인 커버리지 100%
package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WeatherClientTest {

    @Test
    void getTodayWeather_success_returnsWeather() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplate);

        WeatherClient client = new WeatherClient(builder);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        WeatherDto[] body = new WeatherDto[] {
                new WeatherDto(today, "SUNNY")
        };

        when(restTemplate.getForEntity(any(URI.class), eq(WeatherDto[].class)))
                .thenReturn(ResponseEntity.ok(body));

        String res = client.getTodayWeather();

        assertThat(res).isEqualTo("SUNNY");
        verify(restTemplate).getForEntity(any(URI.class), eq(WeatherDto[].class));
    }

    @Test
    void getTodayWeather_statusNotOk_throws() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplate);

        WeatherClient client = new WeatherClient(builder);

        WeatherDto[] body = new WeatherDto[] { new WeatherDto("01-01", "SUNNY") };

        when(restTemplate.getForEntity(any(URI.class), eq(WeatherDto[].class)))
                .thenReturn(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));

        assertThatThrownBy(client::getTodayWeather)
                .isInstanceOf(ServerException.class)
                .hasMessageContaining("상태 코드:");
    }

    @Test
    void getTodayWeather_bodyNullOrEmpty_throws() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplate);

        WeatherClient client = new WeatherClient(builder);

        when(restTemplate.getForEntity(any(URI.class), eq(WeatherDto[].class)))
                .thenReturn(ResponseEntity.ok(new WeatherDto[]{}));

        assertThatThrownBy(client::getTodayWeather)
                .isInstanceOf(ServerException.class)
                .hasMessage("날씨 데이터가 없습니다.");
    }

    @Test
    void getTodayWeather_todayNotFound_throws() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplate);

        WeatherClient client = new WeatherClient(builder);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        String notToday = today.equals("01-01") ? "01-02" : "01-01";

        WeatherDto[] body = new WeatherDto[] {
                new WeatherDto(notToday, "CLOUDY")
        };

        when(restTemplate.getForEntity(any(URI.class), eq(WeatherDto[].class)))
                .thenReturn(ResponseEntity.ok(body));

        assertThatThrownBy(client::getTodayWeather)
                .isInstanceOf(ServerException.class)
                .hasMessage("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.");
    }
}