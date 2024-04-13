package com.example.cinemapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class IniciarSesion extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion);
        Button iniciar = findViewById(R.id.iniciarInicio);
        iniciar.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            TextView escrNom = (TextView) findViewById(R.id.escribirNomInicio);
            TextView escrCont = (TextView) findViewById(R.id.escribirContInicio);
            // Crear un objeto Data con la operación que quieres realizar
            Data inputData = new Data.Builder()
                    .putString("operation", "obtenerUsuario")
                    .putString("nombre",escrNom.getText().toString())
                    .putString("contrasena",escrCont.getText().toString())

                    .build();


            // Crear una tarea OneTimeWorkRequest para ejecutar MyWorker
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                    .setInputData(inputData)
                    .build();

            // Programar la tarea para que se ejecute con el WorkManager
            WorkManager.getInstance().enqueue(workRequest);
        }
    });





    }
}
