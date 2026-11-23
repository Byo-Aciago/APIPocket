package com.example.apipocket;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apipocket.api.OpenMeteoApiService;
import com.example.apipocket.api.RetrofitClient;
import com.example.apipocket.helper.WeatherHelper;
import com.example.apipocket.model.weather.CurrentWeather;
import com.example.apipocket.model.weather.DailyWeather;
import com.example.apipocket.model.weather.WeatherResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvCurrentTemp;
    private TextView tvWeatherEmoji;
    private TextView tvCurrentHumidity;
    private TextView tvApparentTemp;
    private TextView tvWindSpeed;
    private TextView tvWeatherCode;
    private TextView tvForecastDay1;
    private TextView tvForecastDay2;
    private TextView tvForecastDay3;
    private Button btnReload;

    private static final double LATITUDE = 14.6349;
    private static final double LONGITUDE = -90.5069;
    private static final String TIMEZONE = "auto";
    private static final String CURRENT_PARAMS = "temperature_2m,relative_humidity_2m,weather_code,apparent_temperature,wind_speed_10m,is_day";
    private static final String DAILY_PARAMS = "temperature_2m_max,temperature_2m_min,precipitation_probability_max";
    private static final int FORECAST_DAYS = 3;

    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Clima (Open-Meteo)");
        }

        progressBar = findViewById(R.id.progressBarWeather);
        tvError = findViewById(R.id.tvErrorWeather);
        tvCurrentTemp = findViewById(R.id.tvCurrentTemp);
        tvWeatherEmoji = findViewById(R.id.tvWeatherEmoji);
        tvCurrentHumidity = findViewById(R.id.tvCurrentHumidity);
        tvApparentTemp = findViewById(R.id.tvApparentTemp);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvWeatherCode = findViewById(R.id.tvWeatherCode);
        tvForecastDay1 = findViewById(R.id.tvForecastDay1);
        tvForecastDay2 = findViewById(R.id.tvForecastDay2);
        tvForecastDay3 = findViewById(R.id.tvForecastDay3);
        btnReload = findViewById(R.id.btnReloadWeather);

        Button btnBack = findViewById(R.id.btnBackWeather);
        Button btnBackToMain = findViewById(R.id.btnBackToMainWeather);

        View.OnClickListener backListener = v -> finish();
        if (btnBack != null) btnBack.setOnClickListener(backListener);
        if (btnBackToMain != null) btnBackToMain.setOnClickListener(backListener);

        btnReload.setOnClickListener(v -> fetchWeatherData());

        fetchWeatherData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchWeatherData() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnReload.setEnabled(false);

        OpenMeteoApiService apiService = RetrofitClient.getOpenMeteoClient().create(OpenMeteoApiService.class);
        Call<WeatherResponse> call = apiService.getWeatherForecast(
                LATITUDE, LONGITUDE, TIMEZONE, CURRENT_PARAMS, DAILY_PARAMS, FORECAST_DAYS
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    updateUI(weatherResponse);
                } else {
                    showError("Error al obtener los datos del clima (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);
                showError("Error de conexión: " + t.getLocalizedMessage());
            }
        });
    }

    private void updateUI(WeatherResponse response) {
        CurrentWeather current = response.getCurrent();
        if (current != null) {
            tvCurrentTemp.setText(String.format(Locale.getDefault(), "%.1f °C", current.getTemperature()));

            boolean isDay = current.getIsDay() == 1;
            WeatherHelper.WeatherInfo info = WeatherHelper.getWeatherInfo(current.getWeatherCode(), isDay);

            tvWeatherEmoji.setText(info.getEmoji());
            tvWeatherCode.setText(info.getDescription());
            tvCurrentHumidity.setText(String.format(Locale.getDefault(), "💧 Humedad: %d%%", current.getHumidity()));
            tvApparentTemp.setText(String.format(Locale.getDefault(), "🌡️ Sensación: %.1f °C", current.getApparentTemperature()));
            tvWindSpeed.setText(String.format(Locale.getDefault(), "💨 Viento: %.1f km/h", current.getWindSpeed()));
        }

        DailyWeather daily = response.getDaily();
        if (daily != null) {
            List<String> dates = daily.getTime();
            List<Double> maxTemps = daily.getTempMax();
            List<Double> minTemps = daily.getTempMin();
            List<Integer> precipProbs = daily.getPrecipitationProbabilityMax();

            TextView[] forecastViews = {tvForecastDay1, tvForecastDay2, tvForecastDay3};

            if (dates != null && maxTemps != null && minTemps != null) {
                for (int i = 0; i < Math.min(3, dates.size()); i++) {
                    String dateStr = dates.get(i);
                    String formattedDate = dateStr;
                    try {
                        Date parsedDate = inputDateFormat.parse(dateStr);
                        if (parsedDate != null) {
                            formattedDate = outputDateFormat.format(parsedDate);
                        }
                    } catch (ParseException ignored) {}

                    double min = i < minTemps.size() ? minTemps.get(i) : 0;
                    double max = i < maxTemps.size() ? maxTemps.get(i) : 0;
                    int precip = (precipProbs != null && i < precipProbs.size()) ? precipProbs.get(i) : 0;

                    WeatherHelper.WeatherInfo info = WeatherHelper.getWeatherInfo(current != null ? current.getWeatherCode() : 0, true);

                    String dayText = String.format(Locale.getDefault(),
                            "📅 %s  %s\n   Mín: %.1f°C - Máx: %.1f°C  •  🌧️ Lluvia: %d%%",
                            formattedDate, info.getEmoji(), min, max, precip);

                    forecastViews[i].setText(dayText);
                }
            }
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}
