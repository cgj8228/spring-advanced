// 목적: WeatherDto 생성자/게터 라인 커버리지 100%
package org.example.expert.client.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherDtoTest {

    @Test
    void getters_work() {
        WeatherDto dto = new WeatherDto("01-01", "SUNNY");
        assertThat(dto.getDate()).isEqualTo("01-01");
        assertThat(dto.getWeather()).isEqualTo("SUNNY");
    }
}