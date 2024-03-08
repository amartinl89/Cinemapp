package com.example.cinemapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class AnadirPelicula extends AppCompatActivity {
    private ActivityResultLauncher<String> pickImageLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_pelicula);
        anadirAnos(findViewById(R.id.escriibirAnoPeliV));
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
        for (int i=1895; i<2025;i++){
            l.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
}
