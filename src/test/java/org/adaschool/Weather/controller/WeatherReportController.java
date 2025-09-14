package org.adaschool.Weather.controller;

import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WeatherReportController.class)
class WeatherReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherReportService weatherReportService;

    @Test
    @DisplayName("GET /v1/api/weather-report retorna 200 y el JSON esperado")
    void getWeatherReport_returnsOkWithBody() throws Exception {
        // arrange
        double lat = 37.8267;
        double lon = -122.4233;

        WeatherReport report = new WeatherReport();
        report.setTemperature(20.5);
        report.setHumidity(72.0);

        Mockito.when(weatherReportService.getWeatherReport(lat, lon)).thenReturn(report);

        // act & assert
        mockMvc.perform(get("/v1/api/weather-report")
                        .param("latitude", String.valueOf(lat))
                        .param("longitude", String.valueOf(lon)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.temperature").value(20.5))
                .andExpect(jsonPath("$.humidity").value(72.0));
    }
}
