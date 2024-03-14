package com.example.cinemapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_IDIOMA = "idioma";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        setIdioma();



        setContentView(R.layout.activity_main);
        Button botAnadirPelicula = findViewById( R.id.anadirReviewV);
        botAnadirPelicula.setText(getResources().getString(R.string.anadir_peli_str));
        botAnadirPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, AnadirPelicula.class);
                e.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.startActivity(e);
            }
        });
        Button botVerPeliculas = findViewById( R.id.verReviewV);
        botVerPeliculas.setText(getResources().getString(R.string.ver_pelis_str));
        botVerPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, VerPelicula.class);
                MainActivity.this.startActivity(e);
            }
        });

        ImageButton conf = findViewById(R.id.configV);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, Configuracion.class);
                MainActivity.this.startActivity(e);
            }
        });

    }

    private void setIdioma() {
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        String idiomaSeleccionado = prefs.getString(PREF_IDIOMA, "");

        Locale locale = new Locale(idiomaSeleccionado);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

    }
}