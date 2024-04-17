package com.example.cinemapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.firebase.appdistribution.UpdateTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class PeliWidget extends AppWidgetProvider{



    private static class UpdateTask extends TimerTask {
        private final Context context;
        private final AppWidgetManager appWidgetManager;
        private final int[] appWidgetIds;
        private JSONObject peli;

        public UpdateTask(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            this.context = context;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetIds = appWidgetIds;
        }

        @Override
        public void run() {
            // Aquí se realiza la actualización del widget
            try {
                updateWidget();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        private void updateWidget() throws JSONException {
            // Aquí puedes realizar la lógica para cambiar la revisión mostrada en el widget
            // Por ejemplo, obtener una revisión aleatoria
            getRandomReview();

            // Actualizar cada widget individualmente
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }

        private void getRandomReview() throws JSONException {
            if(!context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
                    getBoolean("SESION", false)){
                Data inputData = new Data.Builder()
                        .putString("operation", "visualizarLista")
                        .putString("usuario",context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
                                getString("USUARIO", null))
                        .build();
                OneTimeWorkRequest otwr = new
                        OneTimeWorkRequest.Builder(ConexionBDWebService.class)
                        .setInputData(inputData)
                        .build();
                WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                        .observe((LifecycleOwner) context, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                try {
                                    if (workInfo != null && workInfo.getState().isFinished()) {
                                        Data outputData = workInfo.getOutputData();
                                        String res = outputData.getString("jsonResponse");

                                        JSONArray listaPeliculas = new JSONArray(res);
                                        int randomIndex = new Random().nextInt(listaPeliculas.length());

                                        // Obtener el elemento JSONObject en el índice aleatorio
                                        peli = listaPeliculas.getJSONObject(randomIndex);
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        });
                WorkManager.getInstance().enqueue(otwr);
            }else {
                peli = new JSONObject();
                peli.put("nombre","Inicia sesión");

            }
        }

        private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) throws JSONException {
            // Configurar las vistas del widget con la revisión obtenida
            if(peli.has("imagen")){
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.peli_widget);
                views.setTextViewText(R.id.nomWidget, peli.getString("nombre"));
                String base64 = (String) peli.get("Imagen");
                byte[] imagenBytes = Base64.getDecoder().decode(base64);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                views.setImageViewBitmap(R.id.imgWidget,bitmap);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.peli_widget);
                views.setTextViewText(R.id.nomWidget,"Inicia sesión");

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Este método se llama cuando el widget debe ser actualizado

        // Configura un temporizador para actualizar el widget cada minuto
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTask(context, appWidgetManager, appWidgetIds), 0, 60000); // 60000 ms = 1 minuto
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Este método se llama cuando se crea el primer widget

        // Obtén el AppWidgetManager para interactuar con los widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        // Obten los IDs de los widgets activos actualmente
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, PeliWidget.class));

        // Configura un temporizador para actualizar el widget cada minuto
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTask(context, appWidgetManager, appWidgetIds), 0, 1); // 60000 ms = 1 minuto
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}