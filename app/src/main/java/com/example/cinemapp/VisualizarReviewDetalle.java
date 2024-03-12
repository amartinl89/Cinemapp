package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;

public class VisualizarReviewDetalle extends AppCompatActivity {
    /*private final LocalDate fecha;
    private final String nom;
    private final String ano;
    private final String punt;
    private final Bitmap imagen;
    private final String resena;*/


   /* public  VisualizarReviewDetalle(LocalDate fecha, String nom, String ano, String punt, Bitmap imagen, String resena)
    {
        this.fecha=fecha;
        this.nom = nom;
        this.ano = ano;
        this.punt=punt;
        this.imagen=imagen;
        this.resena=resena;
    }*/
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
//        LocalDate fecha = LocalDate.parse(fechaString);
        String nom = intent.getStringExtra("nom");
        String ano = intent.getStringExtra("ano");
        String punt = intent.getStringExtra("punt");
        byte[] imagenBytes = intent.getByteArrayExtra("imagen");
        Bitmap imagen = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
        String resena = intent.getStringExtra("resena");

        // Establecer los valores en los TextView
        //txtFecha.setText("Fecha: " + fecha.toString()); // Ajusta según el formato que desees
        txtNombre.setText("Nombre: " + nom);
        txtAno.setText("Año: " + ano);
        txtPuntuacion.setText("Puntuación: " + punt);
        txtResena.setText("Reseña: " + resena);
        imgDetalle.setImageBitmap(imagen); // Ajusta según tu lógica para cargar la imagen


    }
}
