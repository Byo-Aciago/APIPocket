package com.example.apipocket.api;

import com.example.apipocket.model.weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenMeteoApiService {

    @GET("v1/forecast")
    Call<WeatherResponse> getWeatherForecast(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("timezone") String timezone,
            @Query("current") String current,
            @Query("daily") String daily,
            @Query("forecast_days") int forecastDays
    );
}
