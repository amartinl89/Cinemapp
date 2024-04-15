package com.example.cinemapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopUpBorrar extends DialogFragment {

    private Intent parentIntent;
    Activity a;
    Intent intent;

    public void setParentIntent(Intent parentIntent) {
        this.parentIntent = parentIntent;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.popupborrar_str));
        builder.setPositiveButton(getResources().getString(R.string.popupborrar_str), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (parentIntent != null) {
                    String fecha = parentIntent.getStringExtra("fecha"); //Se borra
                    Data inputData = new Data.Builder()
                            .putString("operation", "borrarResena")
                            .putString("fecha",fecha)
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
                                        intent.putExtra("nombre",parentIntent.getStringExtra("nombre"));
                                        PopUpBorrar.this.startActivity(intent);

                                    }
                                }
                            });
                    Intent e = new Intent(a,VerPelicula.class);
                    e.putExtra("nombre",parentIntent.getStringExtra("nombre"));
                    PopUpBorrar.this.startActivity(e); //Se recarga la página
                    WorkManager.getInstance().enqueue(otwr);
                    //GestorBD bd = new GestorBD(getContext());
                    //bd.borrarResena(fecha);
                }

            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PopUpBorrar.this.getActivity(); //No se hace nada
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a=(Activity) context;
            Intent intent = new Intent(a,VerPelicula.class);
        }
    }


}
