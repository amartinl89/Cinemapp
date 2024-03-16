package com.example.cinemapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class Configuracion extends AppCompatActivity {

    private static final String PREF_IDIOMA = "idioma";
    private static final String PREF_OSCURO = "oscuro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        RadioButton es = findViewById(R.id.castellanoConfig);
        es.setText(getResources().getString(R.string.es_str));
        RadioButton en = findViewById(R.id.inglesConfig);
        en.setText(getResources().getString(R.string.en_str));
        TextView idioma = findViewById(R.id.camIdiomaConfig);
        idioma.setText(getResources().getString(R.string.language_str));

        Button back = findViewById(R.id.backConfig);
        back.setText(getResources().getString(R.string.back_str));

        Locale currentLocale = getResources().getConfiguration().getLocales().get(0);
        String check = currentLocale.getLanguage();
        //Para seleccionar el switch en el que está el idioma ahora mismo
        if (check.equals("en")) {
            en.setChecked(true);
        } else {
            es.setChecked(true);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(Configuracion.this, MainActivity.class);
                Configuracion.this.startActivity(e);
            }
        });
        SwitchCompat oscuro = findViewById(R.id.oscuroConfig);
        oscuro.setText(getResources().getString(R.string.oscuro_str));
        String temaSeleccionado = getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
                getString(PREF_OSCURO, "null");
        if(temaSeleccionado.equals("1")){
            oscuro.setChecked(true);
        }

        Button confirmar = findViewById(R.id.confirmarConfig);
        confirmar.setText(getResources().getString(R.string.confirmar_str));

        //Para aplicar los cambios
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (es.isChecked()) {
                    cambiarIdioma("es");

                } else {
                    cambiarIdioma("en");
                }
                if (oscuro.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(PREF_OSCURO, "1");
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //Modo nocturno
                }
                else{
                    SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(PREF_OSCURO, "0");
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


    }
    //Función para cambiar el idioma
    private void cambiarIdioma(String languageCode) {
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_IDIOMA, languageCode);
        editor.apply();

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
    }
}