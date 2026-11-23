package com.example.apipocket;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.apipocket.api.NasaApiService;
import com.example.apipocket.api.RetrofitClient;
import com.example.apipocket.model.nasa.NasaModel;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NasaActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvError;
    private MaterialCardView cardContent;
    private TextView tvTitle;
    private TextView tvDate;
    private ImageView ivNasaImage;
    private TextView tvDescription;
    private Button btnReload;

    private static final String TAG = "NASA_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("NASA APOD");
        }

        progressBar = findViewById(R.id.progressBarNasa);
        tvError = findViewById(R.id.tvErrorNasa);
        cardContent = findViewById(R.id.cardNasaContent);
        tvTitle = findViewById(R.id.tvNasaTitle);
        tvDate = findViewById(R.id.tvNasaDate);
        ivNasaImage = findViewById(R.id.ivNasaImage);
        tvDescription = findViewById(R.id.tvNasaDescription);
        btnReload = findViewById(R.id.btnReloadNasa);

        Button btnBack = findViewById(R.id.btnBackNasa);
        Button btnBackToMain = findViewById(R.id.btnBackToMainNasa);

        View.OnClickListener backListener = v -> finish();
        if (btnBack != null) btnBack.setOnClickListener(backListener);
        if (btnBackToMain != null) btnBackToMain.setOnClickListener(backListener);

        btnReload.setOnClickListener(v -> fetchApodData());

        fetchApodData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchApodData() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        cardContent.setVisibility(View.GONE);
        btnReload.setEnabled(false);

        NasaApiService apiService = RetrofitClient.getNasaClient().create(NasaApiService.class);
        Call<List<NasaModel>> call = apiService.getApod(1);

        call.enqueue(new Callback<List<NasaModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<NasaModel>> call, @NonNull Response<List<NasaModel>> response) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    NasaModel item = response.body().get(0);
                    displayApod(item);
                } else {
                    showError("Error al obtener datos de la NASA (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NasaModel>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);
                showError("Error de conexión: " + t.getLocalizedMessage());
            }
        });
    }

    private void displayApod(NasaModel item) {
        if (item.getTitle() != null) {
            tvTitle.setText(parseHtml(item.getTitle()));
        }

        if (item.getDate() != null) {
            tvDate.setText("📅 " + item.getDate());
        }

        String variableUrl = item.getUrl();
        Log.d("NASA_DEBUG", "URL exacta pasada a Glide: " + variableUrl);

        if (variableUrl != null && !variableUrl.isEmpty()) {
            ivNasaImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(variableUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Glide error al cargar imagen desde URL: " + variableUrl, e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            Log.d(TAG, "Glide cargó exitosamente la imagen desde URL: " + variableUrl);
                            return false;
                        }
                    })
                    .into(ivNasaImage);
        } else {
            ivNasaImage.setVisibility(View.GONE);
        }

        String descriptionText = null;
        if (item.getContent() != null && !item.getContent().isEmpty()) {
            descriptionText = item.getContent();
        } else if (item.getExplanation() != null && !item.getExplanation().isEmpty()) {
            descriptionText = item.getExplanation();
        }

        if (descriptionText != null) {
            tvDescription.setText(parseHtml(descriptionText));
        } else {
            tvDescription.setText("Sin descripción disponible.");
        }

        cardContent.setVisibility(View.VISIBLE);
    }

    private String parseHtml(String htmlString) {
        if (htmlString == null) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY).toString().trim();
        } else {
            return Html.fromHtml(htmlString).toString().trim();
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        cardContent.setVisibility(View.GONE);
    }
}
