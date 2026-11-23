package com.example.apipocket;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialCardView cardWeather = findViewById(R.id.cardWeather);
        MaterialCardView cardEarthquake = findViewById(R.id.cardEarthquake);
        MaterialCardView cardNasa = findViewById(R.id.cardNasa);
        MaterialCardView cardNews = findViewById(R.id.cardNews);
        MaterialCardView cardCountry = findViewById(R.id.cardCountry);
        MaterialButton btnInfo = findViewById(R.id.btnInfo);

        cardWeather.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        cardEarthquake.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EarthquakeActivity.class);
            startActivity(intent);
        });

        cardNasa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NasaActivity.class);
            startActivity(intent);
        });

        cardNews.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        cardCountry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            startActivity(intent);
        });

        if (btnInfo != null) {
            btnInfo.setOnClickListener(v -> showAboutDialog());
        }
    }

    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Acerca de API Pocket")
                .setMessage("Bienvenido a la app que transforma datos en experiencias. Aquí no solo consumes información: exploras, interactúas y aprendes cómo el mundo digital se conecta con el real. Explora el mundo en tiempo real, una API a la vez.\n\nConsulta el clima de cualquier ciudad, descubre la imagen astronómica del día de la NASA, explora datos de países, monitorea sismos recientes y mantente al día con las noticias. Todo en una interfaz simple, sin registros ni suscripciones.")
                .setPositiveButton("Entendido", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
