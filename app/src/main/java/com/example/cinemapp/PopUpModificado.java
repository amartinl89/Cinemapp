package com.example.cinemapp;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.parseIntent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class PopUpModificado extends DialogFragment {
    String fechaString;
    String nom;
    String ano;
    String punt;
    byte[] imagenBytes;
    String resena;
    String usuario;

    public void setDatos(String fechaString, String nom, String ano, String punt, byte[] imagenBytes, String resena, String usuario) {
        this.fechaString=fechaString;
        this.nom = nom;
        this.ano = ano;
        this.punt = punt;
        this.imagenBytes = imagenBytes;
        this.resena = resena;
        this.usuario = usuario;

    }
    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.popupmodif_str));
        builder.setPositiveButton(getResources().getString(R.string.vale_str),new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //GestorBD bd = new GestorBD(getContext());
                //bd.actualizarReview(fechaString,nom,imagenBytes,Integer.parseInt(ano),resena,Integer.parseInt(punt));
                GestorBD sgbd = new GestorBD(getContext());
                String b64 = Base64.getEncoder().encodeToString(imagenBytes);
                if(sgbd.actualizarImagen(fechaString, b64)==0) {
                    sgbd.insertarImagen(fechaString, b64);
                }
                Data inputData = new Data.Builder()
                        .putString("operation", "actualizarReview")
                        .putString("fecha",fechaString)
                        .putString("nuevoNombre",nom)
                        .putString("nuevaImagen",fechaString)
                        .putString("nuevoAno",ano)
                        .putString("nuevaResena", resena)
                        .putString("nuevaPunt", punt)
                        .build();
                OneTimeWorkRequest otwr = new
                        OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                        .observe(getActivity(), new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                try {
                                    if (workInfo != null && workInfo.getState().isFinished()) {
                                        Data outputData = workInfo.getOutputData();
                                        String res = outputData.getString("jsonResponse");

                                    }
                                } catch (Exception e) {

                                }
                            }
                        });
                Intent e = new Intent(getActivity(), VerPelicula.class);
                e.putExtra("nombre",usuario);
                PopUpModificado.this.startActivity(e); //Se vuelve a la vista verpeliculas
                WorkManager.getInstance().enqueue(otwr);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PopUpModificado.this.getActivity(); //No se hace nada
            }
        });

        return builder.create();
    }
}
