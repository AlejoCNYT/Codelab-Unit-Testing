package org.adaschool.Weather.data;

public class WeatherReport {

    private double temperature;
    private double humidity;

    public WeatherReport() {}

    public WeatherReport(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

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

    @Override
    public String toString() {
        return "WeatherReport{temperature=" + temperature + ", humidity=" + humidity + '}';
    }
}
