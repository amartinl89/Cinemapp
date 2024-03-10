package com.example.cinemapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PopUpCreadoVacio extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Añade un nombre y una portada a la película"); //Cambiar por strings
        //Cambiar por strings
        builder.setNeutralButton("Vale",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent e = new Intent(getActivity(), AnadirPelicula.class);
                PopUpCreadoVacio.this.getActivity();
            }
        });

        return builder.create();
    }
}
