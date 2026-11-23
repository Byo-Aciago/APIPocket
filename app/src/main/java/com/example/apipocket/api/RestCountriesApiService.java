package com.example.apipocket.api;

import com.example.apipocket.model.country.CountryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestCountriesApiService {

    @GET("countries/v5/names.common")
    Call<CountryResponse> getCountriesByName(
            @Query("q") String query
    );
}
