package com.example.apipocket.model.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DailyWeather {
    @SerializedName("time")
    private List<String> time;

    @SerializedName("temperature_2m_max")
    private List<Double> tempMax;

    @SerializedName("temperature_2m_min")
    private List<Double> tempMin;

    @SerializedName("precipitation_probability_max")
    private List<Integer> precipitationProbabilityMax;

    public List<String> getTime() {
        return time;
    }

    public List<Double> getTempMax() {
        return tempMax;
    }

    public List<Double> getTempMin() {
        return tempMin;
    }

    public List<Integer> getPrecipitationProbabilityMax() {
        return precipitationProbabilityMax;
    }
}
