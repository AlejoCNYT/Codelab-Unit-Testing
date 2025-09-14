package org.adaschool.Weather.service;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WeatherReportServiceTest {

    @Test
    @DisplayName("getWeatherReport arma la URL, consulta la API y mapea temperatura/humedad")
    void getWeatherReport_mapsResponseCorrectly() {
        // Fake response desde la API
        WeatherApiResponse.Main main = new WeatherApiResponse.Main();
        main.setTemperature(293.15); // valor de ejemplo
        main.setHumidity(60.0);

        WeatherApiResponse apiResponse = new WeatherApiResponse();
        apiResponse.setMain(main);

        // Interceptamos el new RestTemplate() que ocurre dentro del método
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(
                RestTemplate.class,
                (mock, context) -> when(mock.getForObject(anyString(), eq(WeatherApiResponse.class)))
                        .thenReturn(apiResponse)
        )) {
            WeatherReportService service = new WeatherReportService();

            // act
            WeatherReport report = service.getWeatherReport(10.0, 20.0);

            // assert
            assertEquals(293.15, report.getTemperature(), 0.0001);
            assertEquals(60.0, report.getHumidity(), 0.0001);

            // además validamos que realmente se llamó a la API
            RestTemplate created = mocked.constructed().get(0);
            verify(created, times(1)).getForObject(anyString(), eq(WeatherApiResponse.class));
        }
    }
}
