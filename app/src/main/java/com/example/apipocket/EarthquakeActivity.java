package com.example.apipocket;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apipocket.adapter.EarthquakeAdapter;
import com.example.apipocket.api.RetrofitClient;
import com.example.apipocket.api.UsgsApiService;
import com.example.apipocket.model.earthquake.EarthquakeFeature;
import com.example.apipocket.model.earthquake.EarthquakeResponse;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvEmpty;
    private RecyclerView rvEarthquakes;
    private Button btnReload;
    private ChipGroup chipGroupFilter;
    private EarthquakeAdapter adapter;

    private final List<EarthquakeFeature> allEarthquakesList = new ArrayList<>();
    private final List<EarthquakeFeature> displayedEarthquakesList = new ArrayList<>();

    private static final String FORMAT = "geojson";
    private static final String START_TIME = "now-7days";
    private static final double MIN_MAGNITUDE = 3.0;
    private static final double LATITUDE = 14.6349;
    private static final double LONGITUDE = -90.5069;
    private static final double MAX_RADIUS_KM = 500.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sismos (USGS)");
        }

        progressBar = findViewById(R.id.progressBarEarthquake);
        tvError = findViewById(R.id.tvErrorEarthquake);
        tvEmpty = findViewById(R.id.tvEmptyEarthquake);
        rvEarthquakes = findViewById(R.id.rvEarthquakes);
        btnReload = findViewById(R.id.btnReloadEarthquake);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);

        Button btnBack = findViewById(R.id.btnBackEarthquake);
        Button btnBackToMain = findViewById(R.id.btnBackToMainEarthquake);

        View.OnClickListener backListener = v -> finish();
        if (btnBack != null) btnBack.setOnClickListener(backListener);
        if (btnBackToMain != null) btnBackToMain.setOnClickListener(backListener);

        rvEarthquakes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EarthquakeAdapter(displayedEarthquakesList);
        rvEarthquakes.setAdapter(adapter);

        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> filterData());

        btnReload.setOnClickListener(v -> fetchEarthquakes());

        fetchEarthquakes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchEarthquakes() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        btnReload.setEnabled(false);

        UsgsApiService apiService = RetrofitClient.getUsgsClient().create(UsgsApiService.class);
        Call<EarthquakeResponse> call = apiService.getEarthquakes(
                FORMAT, START_TIME, MIN_MAGNITUDE, LATITUDE, LONGITUDE, MAX_RADIUS_KM
        );

        call.enqueue(new Callback<EarthquakeResponse>() {
            @Override
            public void onResponse(@NonNull Call<EarthquakeResponse> call, @NonNull Response<EarthquakeResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    EarthquakeResponse earthquakeResponse = response.body();
                    List<EarthquakeFeature> features = earthquakeResponse.getFeatures();

                    allEarthquakesList.clear();
                    if (features != null) {
                        allEarthquakesList.addAll(features);
                    }
                    filterData();
                } else {
                    showError("Error al obtener sismos (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EarthquakeResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);
                showError("Error de conexión: " + t.getLocalizedMessage());
            }
        });
    }

    private void filterData() {
        boolean filterGuatemala = chipGroupFilter.getCheckedChipId() == R.id.chipGuatemala;

        displayedEarthquakesList.clear();

        for (EarthquakeFeature feature : allEarthquakesList) {
            if (feature != null && feature.getProperties() != null) {
                String place = feature.getProperties().getPlace();
                if (filterGuatemala) {
                    if (place != null && place.toLowerCase(Locale.ROOT).contains("guatemala")) {
                        displayedEarthquakesList.add(feature);
                    }
                } else {
                    displayedEarthquakesList.add(feature);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (displayedEarthquakesList.isEmpty()) {
            if (filterGuatemala) {
                tvEmpty.setText("No se registraron sismos en Guatemala en los últimos 7 días.");
            } else {
                tvEmpty.setText("No se encontraron sismos en el radio de 500 km en los últimos 7 días.");
            }
            tvEmpty.setVisibility(View.VISIBLE);
            rvEarthquakes.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvEarthquakes.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        rvEarthquakes.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }
}
