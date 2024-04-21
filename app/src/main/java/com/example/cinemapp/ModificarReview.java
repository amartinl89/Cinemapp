package com.example.cinemapp;

import static java.lang.Integer.parseInt;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        TextView verAno = (TextView) findViewById(R.id.anoAnadirPeliv);
        verAno.setText(getResources().getString(R.string.ano_peli_str));
        TextView verNom = (TextView) findViewById(R.id.nomAnadirPeliV);
        verNom.setText(getResources().getString(R.string.nom_peli_str));
        TextView verImagen = (TextView) findViewById(R.id.anadirImagenPeliV);
        verImagen.setText(getResources().getString(R.string.anadir_portada_str));
        TextView verPunt = (TextView) findViewById(R.id.puntAnadirPeliV);
        verPunt.setText(getResources().getString(R.string.punt_peli_str));
        TextView verReview = (TextView) findViewById(R.id.reviewAnadirPeliV);
        verReview.setText(getResources().getString(R.string.anadir_review_str));
        if (savedInstanceState !=null){
            txtNombre.setText(savedInstanceState.getString("nom"));
            txtAno.setSelection(2024-savedInstanceState.getInt("ano"));
            txtPuntuacion.setSelection(savedInstanceState.getInt("punt"));
            txtResena.setText(savedInstanceState.getString("review"));
            if (savedInstanceState.getByteArray("img")!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray("img")
                        , 0, savedInstanceState.getByteArray("img").length);
                imgDetalle.setImageBitmap(bitmap);
            }
        }
        Intent intent = getIntent();
            String fechaString = intent.getStringExtra("fecha");
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
            txtAno.setSelection(2024-parseInt(ano));
        }
        if(punt!=null) {
            txtPuntuacion.setSelection(parseInt(punt));
        }


        Button insImg = findViewById(R.id.escribirImagenAnadirPeliV);
        insImg.setVisibility(View.VISIBLE);
        insImg.setText(getResources().getString(R.string.select_img_str));

        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        handleImageResult(result.getData().getData());
                    }
                }
        );

        ActivityResultLauncher<Intent> takePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap laminiatura = (Bitmap) bundle.get("data");
//GUARDAR COMO FICHERO
// Memoria externa
                        File eldirectorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        String nombrefichero = "IMG_" + timeStamp + "_";
                        File imagenFich = new File(eldirectorio, nombrefichero + ".jpg");
                        OutputStream os;
                        try {
                            os = new FileOutputStream(imagenFich);
                            laminiatura.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            ImageView imageView = findViewById(R.id.visualImgAnadirPeliV);
                            imageView.setImageBitmap(laminiatura);
                            os.flush();
                            os.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        //Intent implícito para selecionar imágenes
        /*insImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                pickImageLauncher.launch(intent);
            }
        });*/


        insImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getResources().getString(R.string.select_img_str));
                builder.setItems(
                        new CharSequence[]{getResources().getString(R.string.gal_str),
                                getResources().getString(R.string.cam_str)},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    // Abrir galería
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    galleryIntent.setType("image/*");
                                    pickImageLauncher.launch(galleryIntent);
                                    break;
                                case 1:
                                    // Abrir cámara
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    takePictureLauncher.launch(cameraIntent);

                                    break;
                            }
                        });
                builder.show();
            }
        });
        Button back = findViewById(R.id.backAnadirPeliV);
        back.setText(getResources().getString(R.string.back_str));
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
                e.putExtra("nombre",getIntent().getStringExtra("nombre"));
                ModificarReview.this.startActivity(e);
            }
        });

        Button conf = findViewById(R.id.confirmarAnadirPeliV);
        conf.setText(getResources().getString(R.string.confirmar_str));
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
                TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
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
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(img, 100, 200, false);
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    String fechaString = intent.getStringExtra("fecha");

                    if(!noms.equals("")) {
                        Intent e = new Intent(ModificarReview.this, ModificarReview.class);
                        DialogFragment popup = new PopUpModificado();
                        ((PopUpModificado) popup).setDatos(fechaString,noms,String.valueOf(anoi)
                                ,String.valueOf(punti),byteArray,review,getIntent().getStringExtra("nombre"));
                        popup.show(getSupportFragmentManager(), "modificado");
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
        TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
        ImageView imgI = (ImageView)findViewById(R.id.visualImgAnadirPeliV);
        TextView reviewT = (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
        Spinner puntI = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
        anoi = (Integer) anoS.getSelectedItem();
        noms = nomT.getText().toString();
        review = reviewT.getText().toString();
        punti = (Integer) puntI.getSelectedItem();
        try {
            Bitmap img = ((BitmapDrawable) imgI.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(img, 200, 200, false);
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            outState.putByteArray("img",byteArray);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        outState.putInt("ano",(Integer) anoS.getSelectedItem());
        outState.putString("review",reviewT.getText().toString());
        outState.putInt("punt",(Integer) puntI.getSelectedItem());
        outState.putString("nom",nomT.getText().toString());

    }
}
