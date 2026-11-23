package com.example.apipocket.api;

import android.util.Log;

import com.example.apipocket.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitOpenMeteo = null;
    private static Retrofit retrofitUsgs = null;
    private static Retrofit retrofitNasa = null;
    private static Retrofit retrofitNews = null;
    private static Retrofit retrofitRestCountries = null;

    public static Retrofit getOpenMeteoClient() {
        if (retrofitOpenMeteo == null) {
            retrofitOpenMeteo = new Retrofit.Builder()
                    .baseUrl("https://api.open-meteo.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitOpenMeteo;
    }

    public static Retrofit getUsgsClient() {
        if (retrofitUsgs == null) {
            retrofitUsgs = new Retrofit.Builder()
                    .baseUrl("https://earthquake.usgs.gov/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitUsgs;
    }

    public static Retrofit getNasaClient() {
        if (retrofitNasa == null) {
            retrofitNasa = new Retrofit.Builder()
                    .baseUrl("https://science.nasa.gov/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitNasa;
    }

    public static Retrofit getNewsClient() {
        if (retrofitNews == null) {
            retrofitNews = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitNews;
    }

    public static Retrofit getRestCountriesClient() {
        if (retrofitRestCountries == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            String key = BuildConfig.REST_COUNTRIES_API_KEY;

                            Log.d("COUNTRY_DEBUG", "BuildConfig.REST_COUNTRIES_API_KEY = " +
                                    (key == null ? "NULL" : (key.isEmpty() ? "EMPTY" : "length:" + key.length() + " prefix:" + (key.length() > 6 ? key.substring(0, 6) : key))));

                            Request.Builder requestBuilder = original.newBuilder();
                            if (key != null && !key.isEmpty()) {
                                requestBuilder.header("X-Api-Key", key);
                                requestBuilder.header("x-api-key", key);
                                requestBuilder.header("Authorization", "Bearer " + key);

                                HttpUrl originalHttpUrl = original.url();
                                if (originalHttpUrl.queryParameter("apiKey") == null && originalHttpUrl.queryParameter("api_key") == null) {
                                    HttpUrl url = originalHttpUrl.newBuilder()
                                            .addQueryParameter("apiKey", key)
                                            .build();
                                    requestBuilder.url(url);
                                }
                            }
                            Request request = requestBuilder.build();

                            Log.d("COUNTRY_DEBUG", "Enviando petición a URL: " + request.url());
                            Log.d("COUNTRY_DEBUG", "Headers enviados: " + request.headers());

                            Response response = chain.proceed(request);
                            Log.d("COUNTRY_DEBUG", "Código HTTP respuesta: " + response.code());
                            return response;
                        }
                    })
                    .build();

            retrofitRestCountries = new Retrofit.Builder()
                    .baseUrl("https://api.restcountries.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitRestCountries;
    }
}
