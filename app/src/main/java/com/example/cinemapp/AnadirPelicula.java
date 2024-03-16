package com.example.cinemapp;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AnadirPelicula extends AppCompatActivity {
    Integer ano;
    String nom ;
    String review;
    Integer punt ;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_pelicula);
        anadirAnos(findViewById(R.id.escriibirAnoPeliV));
        anadirPunt(findViewById(R.id.escribirPuntAnadirPeliV));
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
            TextView aux =  (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
            aux.setText(savedInstanceState.getString("review"));
            Spinner spi = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
            spi.setSelection(2024-savedInstanceState.getInt("ano"));
            Spinner puntI = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
            puntI.setSelection(savedInstanceState.getInt("punt"));
            TextView nomx = (TextView) findViewById((R.id.escribirNomAnadirPeliV));
            nomx.setText(savedInstanceState.getString("nom"));
            if (savedInstanceState.getByteArray("img")!=null) {
                ImageView visImg = (ImageView) findViewById(R.id.visualImgAnadirPeliV);
                Bitmap bitmap = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray("img")
                        , 0, savedInstanceState.getByteArray("img").length);
                visImg.setImageBitmap(bitmap);
            }
        }
        Button insImg = findViewById(R.id.escribirImagenAnadirPeliV);
        insImg.setText(getResources().getString(R.string.select_img_str));

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


        //Botón back
        Button back = findViewById(R.id.backAnadirPeliV);
        back.setText(getResources().getString(R.string.back_str));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(AnadirPelicula.this, MainActivity.class);
                AnadirPelicula.this.startActivity(e);
            }
        });

        //Confirmar
        Button confirmar = findViewById(R.id.confirmarAnadirPeliV);
        confirmar.setText(getResources().getString(R.string.confirmar_str));
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
                TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                // Formatear la fecha y hora actual en el formato deseado
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                String formattedDate = dateFormat.format(currentDate);
                ImageView imgI = (ImageView)findViewById(R.id.visualImgAnadirPeliV);
                TextView reviewT = (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
                Spinner puntI = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
                Configuration configuration =
                        getBaseContext().getResources().getConfiguration();
                Context context =
                        getBaseContext().createConfigurationContext(configuration);
                GestorBD bd = new GestorBD(context);
                try {
                    ano = (Integer) anoS.getSelectedItem();
                    nom = nomT.getText().toString();
                    review = reviewT.getText().toString();
                    punt = (Integer) puntI.getSelectedItem();
                    Bitmap img = ((BitmapDrawable) imgI.getDrawable()).getBitmap();
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(img, 200, 200, false);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    if(!nom.equals("")) {
                        bd.insertarReview(formattedDate, nom, byteArray, ano, review, punt);
                        img.recycle();
                        resizedBitmap.recycle();
                        DialogFragment popup = new PopUpCreado();
                        NotificacionIncompleta noti = new NotificacionIncompleta();
                        if(review.equals("")) {
                            noti.mostrarNotificacion(context,nom);
                        }
                        popup.show(getSupportFragmentManager(), "creado");
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
    private void anadirPunt(Spinner s){
        ArrayList<Integer> l= new ArrayList<>();
        for (int i=0; i<11;i++){
            l.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Spinner anoS = (Spinner)findViewById(R.id.escriibirAnoPeliV);
        TextView nomT = (TextView)findViewById(R.id.escribirNomAnadirPeliV);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        // Formatear la fecha y hora actual en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String formattedDate = dateFormat.format(currentDate);
        ImageView imgI = (ImageView)findViewById(R.id.visualImgAnadirPeliV);
        TextView reviewT = (TextView)findViewById(R.id.escribirReviewAnadirPeliV);
        Spinner puntI = (Spinner)findViewById(R.id.escribirPuntAnadirPeliV);
        outState.putInt("ano",(Integer) anoS.getSelectedItem());
        outState.putString("review",reviewT.getText().toString());
        outState.putString("nom",nomT.getText().toString());
        try {
            Bitmap img = ((BitmapDrawable) imgI.getDrawable()).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(img, 200, 200, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            outState.putByteArray("img",byteArray);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        outState.putInt("punt",(Integer) puntI.getSelectedItem());
    }
}
