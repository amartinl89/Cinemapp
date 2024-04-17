package com.example.cinemapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        if (getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
                getBoolean("SESION", false)){
            Intent e = new Intent(IniciarSesion.this, MainActivity.class);
            e.putExtra("nombre", getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
                    getString("USUARIO", null));
            IniciarSesion.this.startActivity(e);
        }
        setContentView(R.layout.iniciar_sesion);
        TextView nom = findViewById(R.id.nomInicio);
        TextView cont = findViewById(R.id.contInicio);
        nom.setText(getResources().getString(R.string.user_str));
        cont.setText(getResources().getString(R.string.cont_str));
        Button iniciar = findViewById(R.id.iniciarInicio);
        iniciar.setText(getResources().getString(R.string.iniciar_str));
        iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView escrNom = (TextView) findViewById(R.id.escribirNomInicio);
                TextView escrCont = (TextView) findViewById(R.id.escribirContInicio);
                //Se crea un objeto Data con la operación a realizar y con los datos del cuerpo
                Data inputData = new Data.Builder()
                        .putString("operation", "obtenerUsuario")
                        .putString("nombre", escrNom.getText().toString())
                        .putString("contrasena", escrCont.getText().toString())

                        .build();
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
                                        SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putBoolean("SESION", true);
                                        editor.putString("USUARIO",escrNom.getText().toString());
                                        editor.apply();
                                        Intent e = new Intent(IniciarSesion.this, MainActivity.class);
                                        e.putExtra("nombre", escrNom.getText().toString());
                                        e.putExtra("contrasena", escrCont.getText().toString());
                                        IniciarSesion.this.startActivity(e);
                                    }
                                    // Manejar el resultado aquí
                                }
                                } catch (JSONException e) {
                                    DialogFragment popup = new PopUpIniciar();
                                    popup.show(getSupportFragmentManager(), "incorrecto"); //Aparece diálogo creado
                                }

                            }
                        });
                WorkManager.getInstance().enqueue(otwr);



            }
        });
        Button reg = findViewById(R.id.registrarInicio);
        reg.setText(getResources().getString(R.string.regis_str));
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(IniciarSesion.this, Registrar.class);
                IniciarSesion.this.startActivity(e);
            }
        });
    }
}
