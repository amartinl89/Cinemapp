package com.example.cinemapp;

import static java.lang.Integer.parseInt;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModificarReview extends AppCompatActivity {
    EditText txtNombre;
    Spinner txtAno;
    Spinner txtPuntuacion;
    EditText txtResena;
    ImageView imgDetalle;
    Integer anoi;
    String noms ;
    String review;
    Integer punti ;
    byte[] byteArray;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_pelicula);
        txtNombre = findViewById(R.id.escribirNomAnadirPeliV);
        txtAno = findViewById(R.id.escriibirAnoPeliV);
        txtPuntuacion = findViewById(R.id.escribirPuntAnadirPeliV);
        txtResena = findViewById(R.id.escribirReviewAnadirPeliV);
        imgDetalle = findViewById(R.id.visualImgAnadirPeliV);

        Intent intent = getIntent();
        String fechaString = intent.getStringExtra("fecha");
//      //LocalDate fecha = LocalDate.parse(fechaString);
        String nom = intent.getStringExtra("nom");
        String ano = intent.getStringExtra("ano");
        String punt = intent.getStringExtra("punt");
        byte[] imagenBytes = intent.getByteArrayExtra("imagen");
        Bitmap imagen = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
        String resena = intent.getStringExtra("resena");

        txtNombre.setText(nom);
        txtResena.setText(resena);
        imgDetalle.setImageBitmap(imagen);
        anadirAnos(txtAno);
        anadirPunt(txtPuntuacion);
        if(ano!=null) {
            txtAno.setSelection(parseInt(ano)-1895);
        }
        if(punt!=null) {
            txtPuntuacion.setSelection(parseInt(punt));
        }


        Button insImg = findViewById(R.id.escribirImagenAnadirPeliV);
        insImg.setVisibility(View.VISIBLE);

        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        handleImageResult(result.getData().getData());
                    }
                }
        );
        insImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });
        Button back = findViewById(R.id.backAnadirPeliV);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(ModificarReview.this, VisualizarReviewDetalle.class);
                e.putExtra("fecha", fechaString);
                e.putExtra("nom", nom);
                e.putExtra("ano", ano);
                e.putExtra("punt", punt);
                e.putExtra("imagen", imagenBytes);
                e.putExtra("resena", resena);
                ModificarReview.this.startActivity(e);
            }
        });

        Button conf = findViewById(R.id.confirmarAnadirPeliV);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
                TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
                Date tiempo = Calendar.getInstance().getTime();
                ImageView imgI = (ImageView)findViewById(R.id.visualImgAnadirPeliV);
                TextView reviewT = (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
                Spinner puntI = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
                try {
                    anoi = (Integer) anoS.getSelectedItem();
                    noms = nomT.getText().toString();
                    review = reviewT.getText().toString();
                    punti = (Integer) puntI.getSelectedItem();
                    Bitmap img = ((BitmapDrawable) imgI.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    String fechaString = intent.getStringExtra("fecha");

                    if(!nom.equals("")) {
                        Intent e = new Intent(ModificarReview.this, ModificarReview.class);
                        DialogFragment popup = new PopUpModificado();
                        e.putExtra("fecha", fechaString);
                        e.putExtra("nom", noms);
                        e.putExtra("ano", anoi);
                        e.putExtra("punt", punti);
                        e.putExtra("imagen", byteArray);
                        e.putExtra("resena", review);
                        popup.show(getSupportFragmentManager(), "modif");
                    }
                    else{
                        DialogFragment popup = new PopUpCreadoVacio();
                        popup.show(getSupportFragmentManager(), "vacio");
                    }
                }
                catch (NullPointerException n){
                    DialogFragment popup = new PopUpCreadoVacio();
                    popup.show(getSupportFragmentManager(), "vacio");
                }

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
    private void anadirPunt(Spinner s) {
        ArrayList<Integer> l = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            l.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
}
