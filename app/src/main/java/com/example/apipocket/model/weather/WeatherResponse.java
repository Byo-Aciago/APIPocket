package com.example.apipocket.model.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("current")
    private CurrentWeather current;

    @SerializedName("daily")
    private DailyWeather daily;

    public CurrentWeather getCurrent() {
        return current;
    }

    public DailyWeather getDaily() {
        return daily;
    }
}
