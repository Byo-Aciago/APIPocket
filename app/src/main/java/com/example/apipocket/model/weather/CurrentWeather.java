package com.example.apipocket.model.weather;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("temperature_2m")
    private double temperature;

    @SerializedName("relative_humidity_2m")
    private int humidity;

    @SerializedName("weather_code")
    private int weatherCode;

    @SerializedName("apparent_temperature")
    private double apparentTemperature;

    @SerializedName("wind_speed_10m")
    private double windSpeed;

    @SerializedName("is_day")
    private int isDay;

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getIsDay() {
        return isDay;
    }
}
