package com.example.apipocket.api;

import com.example.apipocket.model.nasa.NasaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApiService {

    @GET("wp-json/wp/v2/apod-basic")
    Call<List<NasaModel>> getApod(
            @Query("per_page") int perPage
    );
}
