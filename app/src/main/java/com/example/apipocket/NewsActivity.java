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

import com.example.apipocket.adapter.NewsAdapter;
import com.example.apipocket.api.NewsApiService;
import com.example.apipocket.api.RetrofitClient;
import com.example.apipocket.model.news.NewsArticle;
import com.example.apipocket.model.news.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvEmpty;
    private RecyclerView rvNews;
    private Button btnReload;
    private NewsAdapter adapter;
    private final List<NewsArticle> articleList = new ArrayList<>();

    private static final String QUERY = "noticias";
    private static final String LANGUAGE = "es";
    private static final String SORT_BY = "publishedAt";
    private static final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Noticias (News API)");
        }

        progressBar = findViewById(R.id.progressBarNews);
        tvError = findViewById(R.id.tvErrorNews);
        tvEmpty = findViewById(R.id.tvEmptyNews);
        rvNews = findViewById(R.id.rvNews);
        btnReload = findViewById(R.id.btnReloadNews);

        Button btnBack = findViewById(R.id.btnBackNews);
        Button btnBackToMain = findViewById(R.id.btnBackToMainNews);

        View.OnClickListener backListener = v -> finish();
        if (btnBack != null) btnBack.setOnClickListener(backListener);
        if (btnBackToMain != null) btnBackToMain.setOnClickListener(backListener);

        rvNews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(articleList);
        rvNews.setAdapter(adapter);

        btnReload.setOnClickListener(v -> fetchNews());

        fetchNews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchNews() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        btnReload.setEnabled(false);

        String apiKey = BuildConfig.NEWS_API_KEY;

        NewsApiService apiService = RetrofitClient.getNewsClient().create(NewsApiService.class);
        Call<NewsResponse> call = apiService.getEverything(QUERY, LANGUAGE, SORT_BY, PAGE_SIZE, apiKey);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    NewsResponse newsResponse = response.body();
                    List<NewsArticle> articles = newsResponse.getArticles();

                    articleList.clear();
                    if (articles != null && !articles.isEmpty()) {
                        articleList.addAll(articles);
                        adapter.notifyDataSetChanged();
                        rvNews.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rvNews.setVisibility(View.GONE);
                    }
                } else {
                    showError("Error al obtener noticias (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnReload.setEnabled(true);
                showError("Error de conexión: " + t.getLocalizedMessage());
            }
        });
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        rvNews.setVisibility(View.GONE);
    }
}
