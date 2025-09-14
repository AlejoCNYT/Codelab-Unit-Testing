package org.adaschool.Weather.service;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Requiere en build.gradle:
 *   testImplementation 'org.mockito:mockito-inline:4.11.0'
 *   testImplementation 'org.springframework.boot:spring-boot-starter-test'
 */
class WeatherReportServiceTest {

    @Test
    @DisplayName("getWeatherReport llama a OpenWeather y mapea temperatura/humedad")
    void getWeatherReport_mapsResponseCorrectly() throws Exception {
        // Fake de la respuesta de OpenWeather
        WeatherApiResponse.Main main = new WeatherApiResponse.Main();
        main.setTemperature(25.7);
        main.setHumidity(60.0);
        WeatherApiResponse apiResponse = new WeatherApiResponse();
        apiResponse.setMain(main);

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(
                RestTemplate.class,
                (mock, context) -> when(mock.getForObject(anyString(), eq(WeatherApiResponse.class)))
                        .thenReturn(apiResponse)
        )) {
            // SUT
            WeatherReportService service = new WeatherReportService();
            setPrivate(service, "apiKey", "dummy");
            setPrivate(service, "apiUrl", "https://api.openweathermap.org/data/2.5/weather");
            setPrivate(service, "units", "metric");

            // Act
            WeatherReport report = service.getWeatherReport(10.0, 20.0);

            // Assert: mapeo correcto
            assertEquals(25.7, report.getTemperature(), 1e-6);
            assertEquals(60.0, report.getHumidity(), 1e-6);

            // Assert: URL construida correctamente
            RestTemplate created = mocked.constructed().get(0);
            ArgumentCaptor<String> urlCap = ArgumentCaptor.forClass(String.class);
            verify(created).getForObject(urlCap.capture(), eq(WeatherApiResponse.class));
            String url = urlCap.getValue();
            assertTrue(url.contains("lat=10.0"));
            assertTrue(url.contains("lon=20.0"));
            assertTrue(url.contains("appid=dummy"));
            assertTrue(url.contains("units=metric"));
        }
    }

    @Test
    @DisplayName("Lanza ResponseStatusException cuando 'main' viene nulo")
    void getWeatherReport_throwsWhenMainMissing() throws Exception {
        WeatherApiResponse apiResponseWithoutMain = new WeatherApiResponse(); // main == null

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(
                RestTemplate.class,
                (mock, context) -> when(mock.getForObject(anyString(), eq(WeatherApiResponse.class)))
                        .thenReturn(apiResponseWithoutMain)
        )) {
            WeatherReportService service = new WeatherReportService();
            setPrivate(service, "apiKey", "dummy");
            setPrivate(service, "apiUrl", "https://api.openweathermap.org/data/2.5/weather");
            setPrivate(service, "units", "metric");

            assertThrows(ResponseStatusException.class,
                    () -> service.getWeatherReport(0.0, 0.0));
        }
    }

    // Utilidad para setear campos privados @Value sin usar el contenedor de Spring
    private static void setPrivate(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}
