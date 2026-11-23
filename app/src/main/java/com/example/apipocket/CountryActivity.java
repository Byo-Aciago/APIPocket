package com.example.apipocket;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.apipocket.api.RestCountriesApiService;
import com.example.apipocket.api.RetrofitClient;
import com.example.apipocket.model.country.CountryObject;
import com.example.apipocket.model.country.CountryResponse;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvEmpty;

    private MaterialCardView cardResult;
    private ImageView ivFlag;
    private TextView tvTitle;
    private TextView tvOfficial;
    private TextView tvShortDesc;
    private View dividerDesc;

    private TextView tvCapital;
    private TextView tvRegion;
    private TextView tvPopulation;
    private TextView tvCurrency;
    private TextView tvLanguage;
    private TextView tvCallingCode;
    private TextView tvTimezone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Países (RestCountries)");
        }

        etSearch = findViewById(R.id.etSearchCountry);
        btnSearch = findViewById(R.id.btnSearchCountry);
        progressBar = findViewById(R.id.progressBarCountry);
        tvError = findViewById(R.id.tvErrorCountry);
        tvEmpty = findViewById(R.id.tvEmptyCountry);

        cardResult = findViewById(R.id.cardCountryResult);
        ivFlag = findViewById(R.id.ivCountryFlag);
        tvTitle = findViewById(R.id.tvCountryTitle);
        tvOfficial = findViewById(R.id.tvCountryOfficial);
        tvShortDesc = findViewById(R.id.tvCountryShortDesc);
        dividerDesc = findViewById(R.id.dividerDesc);

        tvCapital = findViewById(R.id.tvCountryCapital);
        tvRegion = findViewById(R.id.tvCountryRegion);
        tvPopulation = findViewById(R.id.tvCountryPopulation);
        tvCurrency = findViewById(R.id.tvCountryCurrency);
        tvLanguage = findViewById(R.id.tvCountryLanguage);
        tvCallingCode = findViewById(R.id.tvCountryCallingCode);
        tvTimezone = findViewById(R.id.tvCountryTimezone);

        Button btnBack = findViewById(R.id.btnBackCountry);
        Button btnBackToMain = findViewById(R.id.btnBackToMainCountry);

        View.OnClickListener backListener = v -> finish();
        if (btnBack != null) btnBack.setOnClickListener(backListener);
        if (btnBackToMain != null) btnBackToMain.setOnClickListener(backListener);

        btnSearch.setOnClickListener(v -> performSearch());

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Cargar Guatemala por defecto
        fetchCountryData("Guatemala");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSearch() {
        String query = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        if (!query.isEmpty()) {
            fetchCountryData(query);
        } else {
            showError("Por favor ingresa un nombre de país.");
        }
    }

    private void fetchCountryData(String query) {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        cardResult.setVisibility(View.GONE);
        btnSearch.setEnabled(false);

        String apiKey = BuildConfig.REST_COUNTRIES_API_KEY;
        Log.d("COUNTRY_DEBUG", "Iniciando consulta para: " + query + " con API key: " + (apiKey == null ? "NULL" : (apiKey.isEmpty() ? "VACIA" : apiKey.substring(0, Math.min(6, apiKey.length())) + "...")));

        RestCountriesApiService apiService = RetrofitClient.getRestCountriesClient().create(RestCountriesApiService.class);
        Call<CountryResponse> call = apiService.getCountriesByName(query);

        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call, @NonNull Response<CountryResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSearch.setEnabled(true);

                Log.d("COUNTRY_DEBUG", "Respuesta HTTP código: " + response.code() + " es exitoso: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<CountryObject> objects = response.body().getData().getObjects();
                    if (objects != null && !objects.isEmpty()) {
                        CountryObject country = objects.get(0);
                        displayCountryData(country);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401 || response.code() == 403) {
                    showError("Error de autenticación (" + response.code() + "): Revisa la API key configurada.");
                } else if (response.code() == 404) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    showError("Error al consultar el país (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSearch.setEnabled(true);
                showError("Error de conexión: " + t.getLocalizedMessage());
            }
        });
    }

    private void displayCountryData(CountryObject country) {
        String code = country.getAlpha2Code();
        String titleStr = country.getCommonName() + (code.isEmpty() ? "" : " (" + code + ")");
        tvTitle.setText(titleStr);

        tvOfficial.setText(country.getOfficialName());

        String shortDesc = country.getShortDescription();
        if (shortDesc != null && !shortDesc.isEmpty()) {
            tvShortDesc.setText(Html.fromHtml(shortDesc, Html.FROM_HTML_MODE_LEGACY));
            tvShortDesc.setVisibility(View.VISIBLE);
            dividerDesc.setVisibility(View.VISIBLE);
        } else {
            tvShortDesc.setVisibility(View.GONE);
            dividerDesc.setVisibility(View.GONE);
        }

        String flagUrl = country.getFlagPngUrl();
        if (flagUrl != null && !flagUrl.isEmpty()) {
            ivFlag.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(flagUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(ivFlag);
        } else {
            ivFlag.setVisibility(View.GONE);
        }

        tvCapital.setText(String.format(Locale.getDefault(), "🏛️ Capital: %s", country.getCapitalName()));

        String reg = country.getRegion();
        String sub = country.getSubregion();
        String regionStr = reg + (sub != null && !sub.isEmpty() ? " / " + sub : "");
        tvRegion.setText(String.format(Locale.getDefault(), "🌎 Región / Subregión: %s", regionStr.isEmpty() ? "N/A" : regionStr));

        tvPopulation.setText(String.format(Locale.getDefault(), "👥 Población: %s habitantes", country.getFormattedPopulation()));
        tvCurrency.setText(String.format(Locale.getDefault(), "💵 Moneda: %s", country.getCurrencyFormatted()));
        tvLanguage.setText(String.format(Locale.getDefault(), "🗣️ Idioma: %s", country.getLanguageName()));
        tvCallingCode.setText(String.format(Locale.getDefault(), "📞 Código Telefónico: %s", country.getCallingCodeFormatted()));
        tvTimezone.setText(String.format(Locale.getDefault(), "⏰ Zona Horaria: %s", country.getTimezoneFormatted()));

        cardResult.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
    }
}
