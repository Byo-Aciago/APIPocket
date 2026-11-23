package com.example.apipocket.api;

import com.example.apipocket.model.earthquake.EarthquakeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsgsApiService {

    @GET("fdsnws/event/1/query")
    Call<EarthquakeResponse> getEarthquakes(
            @Query("format") String format,
            @Query("starttime") String starttime,
            @Query("minmagnitude") double minmagnitude,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("maxradiuskm") double maxradiuskm
    );
}
