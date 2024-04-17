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

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar  extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        Button reg = findViewById(R.id.valeRegistrar);
        reg.setText(getResources().getString(R.string.confirmar_str));
        TextView usu = findViewById(R.id.nombreRegistrar);
        TextView cont = findViewById(R.id.contRegistrar);
        TextView confcont = findViewById(R.id.confContResgistrar);
        usu.setText(getResources().getString(R.string.user_str));
        cont.setText(getResources().getString(R.string.cont_str));
        confcont.setText(getResources().getString(R.string.confcont_str));
        //Añadir strings
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomT = (TextView)findViewById(R.id.escrNombreRegistrar);
                TextView conT = (TextView)findViewById(R.id.escrContRegistrar);
                TextView con2T = (TextView)findViewById(R.id.escrConfContResgistrar);
                String nom = nomT.getText().toString();
                String con = conT.getText().toString();
                String con2 = con2T.getText().toString();


                if(nom.equals("") || con.equals("") ||
                        con2.equals("") || !con.equals(con2)){
                    DialogFragment popup = new PopUpNomCont();
                    popup.show(getSupportFragmentManager(), "regis");
                }else{
                    Data inputData = new Data.Builder()
                            .putString("operation", "comprobarUsuario")
                            .putString("nombre", nom)
                            .build();
                    OneTimeWorkRequest otwr = new
                            OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                            .setInputData(inputData)
                            .build();
                    WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                            .observe(Registrar.this, new Observer<WorkInfo>() {
                                @Override
                                public void onChanged(WorkInfo workInfo) {
                                    try{
                                        if (workInfo != null && workInfo.getState().isFinished()) {
                                            Data outputData = workInfo.getOutputData();
                                            String res = outputData.getString("jsonResponse");
                                            JSONObject j = new JSONObject(res);
                                            if (j.getBoolean("success")) {
                                                Data inputData = new Data.Builder()
                                                        .putString("operation", "insertarUsuario")
                                                        .putString("nombre", nom)
                                                        .putString("contrasena",con)
                                                        .build();
                                                OneTimeWorkRequest otwr = new
                                                        OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                                                        .setInputData(inputData)
                                                        .build();
                                                WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                                                        .observe(Registrar.this, new Observer<WorkInfo>() {
                                                            @Override
                                                            public void onChanged(WorkInfo workInfo) {
                                                                try{
                                                                    if (workInfo != null && workInfo.getState().isFinished()) {
                                                                        Data outputData = workInfo.getOutputData();
                                                                        String res = outputData.getString("jsonResponse");

                                                                        JSONObject j = new JSONObject(res);



                                                                        if (j.getBoolean("success")) {
                                                                            SharedPreferences prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = prefs.edit();
                                                                            editor.putBoolean("SESION", true);
                                                                            editor.putString("USUARIO",nom);
                                                                            editor.apply();
                                                                            Intent e = new Intent(Registrar.this, MainActivity.class);
                                                                            e.putExtra("nombre",nom);
                                                                            e.putExtra("contrasena", con);
                                                                            Registrar.this.startActivity(e);
                                                                        }
                                                                        else{
                                                                            DialogFragment popup = new PopUpUsuExiste();
                                                                            popup.show(getSupportFragmentManager(), "existe");
                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    DialogFragment popup = new PopUpUsuExiste();
                                                                    popup.show(getSupportFragmentManager(), "existe"); //Aparece diálogo creado
                                                                }

                                                            }
                                                        });
                                                WorkManager.getInstance().enqueue(otwr);
                                            }else{
                                                DialogFragment popup = new PopUpUsuExiste();
                                                popup.show(getSupportFragmentManager(), "existe"); //Aparece diálogo creado
                                            }
                                            // Manejar el resultado aquí
                                        }
                                    } catch (JSONException e) {
                                        System.out.println(e);
                                    }

                                }
                            });
                    WorkManager.getInstance().enqueue(otwr);
                }
            }
        });
        Button back = findViewById(R.id.backRegistrar);
        back.setText(getResources().getString(R.string.back_str));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(Registrar.this,IniciarSesion.class);
                Registrar.this.startActivity(e);
            }
        });
    }
}
