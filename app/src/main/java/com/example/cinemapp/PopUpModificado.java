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
    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Se modificará la reseña"); //Cambiar por strings
        Intent intent = getActivity().getIntent();
        //Cambiar por strings
        builder.setPositiveButton("Vale",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String fechaString = intent.getStringExtra("fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String nom = intent.getStringExtra("nom");
                String ano = intent.getStringExtra("ano");
                String punt = intent.getStringExtra("punt");
                byte[] imagenBytes = intent.getByteArrayExtra("imagen");
                String resena = intent.getStringExtra("resena");
                GestorBD bd = new GestorBD(getContext());
                bd.actualizarReview(fechaString,nom,imagenBytes,Integer.parseInt(ano),resena,Integer.parseInt(punt));
                Intent e = new Intent(getActivity(), VerPelicula.class);
                PopUpModificado.this.startActivity(e);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PopUpModificado.this.getActivity();
            }
        });

        return builder.create();
    }
}
