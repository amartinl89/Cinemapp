package com.example.cinemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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
                        .putString("nombre", escrNom.getText().toString())
                        .putString("contrasena", escrCont.getText().toString())

                        .build();


                // Crear una tarea OneTimeWorkRequest para ejecutar MyWorker
            /*OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                    .setInputData(inputData)
                    .build();

            // Programar la tarea para que se ejecute con el WorkManager
            WorkManager.getInstance().enqueue(workRequest);*/
                OneTimeWorkRequest otwr = new
                        OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                        .observe(IniciarSesion.this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                try{
                                if (workInfo != null && workInfo.getState().isFinished()) {
                                    Data outputData = workInfo.getOutputData();
                                    String res = outputData.getString("jsonResponse");

                                    JSONObject j = new JSONObject(res);



                                    if (j.getBoolean("usuario")) {
                                        Intent e = new Intent(IniciarSesion.this, MainActivity.class);
                                        IniciarSesion.this.startActivity(e);
                                    }
                                    // Manejar el resultado aquí
                                } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                                    // Trabajo fallido
                                    // Manejar el error aquí
                                }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        });
                WorkManager.getInstance().enqueue(otwr);
                //           WorkManager.getInstance(this).enqueue(otwr);
// Obtener el resultado de manera síncrona o asíncrona
            /*try {
                WorkInfo workInfo = future.get();
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    Data outputData = workInfo.getOutputData();
                    boolean result = outputData.getBoolean("usuario",false);
                    if(result){
                        Intent e = new Intent(IniciarSesion.this, MainActivity.class);
                        IniciarSesion.this.startActivity(e);
                    }
                    // Manejar el resultado aquí
                } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                    // Trabajo fallido
                    // Manejar el error aquí
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                // Manejar la excepción aquí
            }
        }
    });*/


            }
        });
    }
}
