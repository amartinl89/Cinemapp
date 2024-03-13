package com.example.cinemapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopUpBorrar extends DialogFragment {

    private Intent parentIntent;

    public void setParentIntent(Intent parentIntent) {
        this.parentIntent = parentIntent;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Se borrará la reseña"); //Cambiar por strings
        builder.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (parentIntent != null) {
                    String fecha = parentIntent.getStringExtra("fecha");
                    GestorBD bd = new GestorBD(getContext());
                    bd.borrarResena(fecha);
                }
                PopUpBorrar.this.startActivity(parentIntent);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PopUpBorrar.this.getActivity();
            }
        });
        return builder.create();
    }
}
