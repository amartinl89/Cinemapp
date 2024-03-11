package com.example.cinemapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

public class VerPelicula extends AppCompatActivity {

    private RecyclerView recyclerViewPeliculas;
    private PeliculaAdapter peliculaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_peliculas);

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
    }
}

