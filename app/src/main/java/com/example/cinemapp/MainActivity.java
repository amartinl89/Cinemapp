package com.example.cinemapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_IDIOMA = "idioma";
    private static final String PREF_OSCURO = "oscuro";
    private static final boolean SESION = true;
    private static final String USUARIO = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPreferencias();
        //Se suscribe a FCM
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FCM", "Subscribed to topic: all");
                        // La suscripción fue exitosa
                    } else {
                        Log.e("FCM", "Failed to subscribe to topic: all", task.getException());
                        // La suscripción falló
                    }
                });
        
        Button botAnadirPelicula = findViewById( R.id.anadirReviewV);
        botAnadirPelicula.setText(getResources().getString(R.string.anadir_peli_str));
        botAnadirPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, AnadirPelicula.class);
                e.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                e.putExtra("nombre",getIntent().getStringExtra("nombre") );
                MainActivity.this.startActivity(e);
            }
        });
        Button botVerPeliculas = findViewById( R.id.verReviewV);
        botVerPeliculas.setText(getResources().getString(R.string.ver_pelis_str));
        botVerPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, VerPelicula.class);
                e.putExtra("nombre",getIntent().getStringExtra("nombre"));
                MainActivity.this.startActivity(e);
            }
        });

        ImageButton conf = findViewById(R.id.configV);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MainActivity.this, Configuracion.class);
                e.putExtra("nombre",getIntent().getStringExtra("nombre") );
                MainActivity.this.startActivity(e);
            }
        });

        TextView holaUsu = findViewById(R.id.usuarioReview);
        holaUsu.setText(getResources().getString(R.string.hola_str,getIntent().getStringExtra("nombre")));

        Button cerrar = findViewById(R.id.salirMenu);
        cerrar.setText(getResources().getString(R.string.cerrar_str));
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("SESION", false);
                editor.putString("USUARIO","");
                editor.apply();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
                Intent e = new Intent(MainActivity.this, IniciarSesion.class);
                MainActivity.this.startActivity(e);
            }
        });

    }






    //Función que sirve para aplicar las preferencias del usuario
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