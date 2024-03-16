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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class PopUpModificado extends DialogFragment {
    String fechaString;
    String nom;
    String ano;
    String punt;
    byte[] imagenBytes;
    String resena;

    public void setDatos(String fechaString, String nom, String ano, String punt, byte[] imagenBytes, String resena) {
        this.fechaString=fechaString;
        this.nom = nom;
        this.ano = ano;
        this.punt = punt;
        this.imagenBytes = imagenBytes;
        this.resena = resena;

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
                GestorBD bd = new GestorBD(getContext());
                bd.actualizarReview(fechaString,nom,imagenBytes,Integer.parseInt(ano),resena,Integer.parseInt(punt));
                Intent e = new Intent(getActivity(), VerPelicula.class);
                PopUpModificado.this.startActivity(e); //Se vuelve a la vista verpeliculas
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
