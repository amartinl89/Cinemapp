package com.example.cinemapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botAnadirPelicula = findViewById( R.id.anadirReviewV);
        botAnadirPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, AnadirPelicula.class);
                MainActivity.this.startActivity(e);
            }
        });
        Button botVerPeliculas = findViewById( R.id.verReviewV);
        botVerPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, VerPelicula.class);
                MainActivity.this.startActivity(e);
            }
        });
    }
}