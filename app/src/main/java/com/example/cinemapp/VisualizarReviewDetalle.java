package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;

public class VisualizarReviewDetalle extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_review_detalle);
        TextView txtNombre = findViewById(R.id.nomDetalleV);
        TextView txtAno = findViewById(R.id.anoDetalleV);
        TextView txtPuntuacion = findViewById(R.id.puntDetalleV);
        TextView txtResena = findViewById(R.id.resenaDetalleV);
        ImageView imgDetalle = findViewById(R.id.imgDetalleV);

        Intent intent = getIntent();
        String fechaString = intent.getStringExtra("fecha");
        //LocalDate fecha = LocalDate.parse(fechaString);
        String nom = intent.getStringExtra("nom");
        String ano = intent.getStringExtra("ano");
        String punt = intent.getStringExtra("punt");
        byte[] imagenBytes = intent.getByteArrayExtra("imagen");
        Bitmap imagen = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
        String resena = intent.getStringExtra("resena");

        // Establecer los valores en los TextView
        //txtFecha.setText("Fecha: " + fecha.toString()); // Ajusta según el formato que desees
        txtNombre.setText(getResources().getString(R.string.nom_peli_str) + nom);
        txtAno.setText(getResources().getString(R.string.ano_peli_str) + ano);
        txtPuntuacion.setText(getResources().getString(R.string.punt_peli_str) + punt);
        txtResena.setText(getResources().getString(R.string.review_str) + resena);
        imgDetalle.setImageBitmap(imagen); // Ajusta según tu lógica para cargar la imagen

        Button back = findViewById(R.id.backDetalleV);
        back.setText(getResources().getString(R.string.back_str));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(VisualizarReviewDetalle.this, VerPelicula.class);
                VisualizarReviewDetalle.this.startActivity(e);
            }
        });

        Button modif = findViewById(R.id.modifDetalleV);
        modif.setText(getResources().getString(R.string.modif_str));


        modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(VisualizarReviewDetalle.this, ModificarReview.class);
                e.putExtra("fecha", fechaString);
                e.putExtra("nom", nom);
                e.putExtra("ano", ano);
                e.putExtra("punt", punt);
                e.putExtra("imagen", imagenBytes);
                e.putExtra("resena", resena);
                VisualizarReviewDetalle.this.startActivity(e);
            }
        });

    }
}
