package com.example.cinemapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AnadirPelicula extends AppCompatActivity {
    private ActivityResultLauncher<String> pickImageLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_pelicula);
        anadirAnos(findViewById(R.id.escriibirAnoPeliV));


        //Botón insertar imagen
        Button insImg = findViewById(R.id.escribirImagenAnadirPeliV);
        // Configurar el ActivityResultLauncher para seleccionar una imagen
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        handleImageResult(result);
                    }
                });

        insImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad para seleccionar una imagen
                pickImageLauncher.launch("image/*");
            }
        });

        //Botón back
        Button back = findViewById(R.id.backAnadirPeliV);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(AnadirPelicula.this, MainActivity.class);
                AnadirPelicula.this.startActivity(e);
            }
        });

        //Confirmar
        Button confirmar = findViewById(R.id.confirmarAnadirPeliV);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
                TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
                Date tiempo = Calendar.getInstance().getTime();
                ImageView imgI = (ImageView)findViewById(R.id.visualImgAnadirPeliV);
                TextView reviewT = (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
                Configuration configuration =
                        getBaseContext().getResources().getConfiguration();
                Context context =
                        getBaseContext().createConfigurationContext(configuration);
                GestorBD bd = new GestorBD(context);
                Integer ano =(Integer)anoS.getSelectedItem();
                String nom =nomT.getText().toString();
                String review =reviewT.getText().toString();
                Bitmap img = ((BitmapDrawable)imgI.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bd.insertarReview(tiempo,nom,byteArray,ano,review);
                img.recycle();
                Intent e = new Intent(AnadirPelicula.this, MainActivity.class);
                AnadirPelicula.this.startActivity(e);
            }
        });
    }

    private void handleImageResult(Uri selectedImageUri) {
        try {
            // Obtener la InputStream de la imagen seleccionada
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Decodificar la InputStream en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Mostrar el Bitmap en un ImageView
            ImageView imageView = findViewById(R.id.visualImgAnadirPeliV);
            imageView.setImageBitmap(bitmap);

            // Cerrar la InputStream
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void anadirAnos(Spinner s){
        ArrayList<Integer> l= new ArrayList<>();
        for (int i=2024; i>=1895;i--){
            l.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
}
