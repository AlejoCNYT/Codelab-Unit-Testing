package org.adaschool.Weather.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherApiResponse {

    // El JSON de OpenWeather trae un objeto "main" con temp y humidity
    private Main main;

    public WeatherApiResponse() {}

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public static class Main {

        // En la API real la clave es "temp"; la mapeamos a "temperature"
        @JsonProperty("temp")
        private double temperature;

        @JsonProperty("humidity")
        private double humidity;

        public Main() {}

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }
    }
}
