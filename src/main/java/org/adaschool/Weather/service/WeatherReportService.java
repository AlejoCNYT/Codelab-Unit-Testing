package org.adaschool.Weather.service;

import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherReportService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    @Value("${openweather.api.units:metric}")
    private String units;

    public WeatherReport getWeatherReport(double latitude, double longitude) {
        String url = apiUrl
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&units=" + units
                + "&appid=" + apiKey;

        try {
            RestTemplate restTemplate = new RestTemplate();
            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (response == null || response.getMain() == null) {
                // OpenWeather suele devolver {cod:401,message:"Invalid API key"} sin "main"
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "OpenWeather response missing 'main'. Check API key or coordinates.");
            }

            WeatherReport report = new WeatherReport();
            report.setTemperature(response.getMain().getTemperature());
            report.setHumidity(response.getMain().getHumidity());
            return report;

        } catch (RestClientResponseException ex) {
            // Errores HTTP de OpenWeather (401, 404, 500…)
            HttpStatus status = HttpStatus.resolve(ex.getRawStatusCode());
            if (status == null) status = HttpStatus.BAD_GATEWAY;
            throw new ResponseStatusException(
                    status,
                    "OpenWeather error: " + ex.getResponseBodyAsString(),
                    ex);
        } catch (RestClientException ex) {
            // Timeouts, DNS, etc.
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error calling OpenWeather: " + ex.getMessage(),
                    ex);
        }
    }
}
