package com.example.cinemapp;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class VerPelicula extends AppCompatActivity {

    private RecyclerView recyclerViewPeliculas;
    private PeliculaAdapter peliculaAdapter;
    private static final String PREF_IDIOMA = "idioma";
    private static final String PREF_OSCURO = "oscuro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_peliculas);
        setPreferencias();

        recyclerViewPeliculas = findViewById(R.id.recyclerViewPeliculas);
        try {
            GestorBD gestorBD = new GestorBD(this);
            JSONArray listaPeliculas = gestorBD.visualizarLista();

            // Configurar el RecyclerView
            recyclerViewPeliculas.setLayoutManager(new LinearLayoutManager(this));
            peliculaAdapter = new PeliculaAdapter(listaPeliculas);
            recyclerViewPeliculas.setAdapter(peliculaAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Botón back
        Button back = findViewById(R.id.backVerPeliculaV);
        back.setText(getResources().getString(R.string.back_str));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(VerPelicula.this, MainActivity.class);
                VerPelicula.this.startActivity(e);
            }
        });
    }
    private void setPreferencias() {
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        String idiomaSeleccionado = prefs.getString(PREF_IDIOMA, "null");
        if(!idiomaSeleccionado.equals("null")) {
            Locale locale = new Locale(idiomaSeleccionado);
            Locale.setDefault(locale);
            Configuration configuration = getResources().getConfiguration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
        String temaSeleccionado = prefs.getString(PREF_OSCURO, "null");
        if(!temaSeleccionado.equals("null")){
            if(temaSeleccionado.equals("0")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }
}


