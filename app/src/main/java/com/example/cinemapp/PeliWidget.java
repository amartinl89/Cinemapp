package com.example.cinemapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of App Widget functionality.
 */
public class PeliWidget extends AppWidgetProvider{

    private Timer updateTimer;
    private static final String ACTION_UPDATE_WIDGET = "com.example.cinemapp.action.UPDATE_WIDGET";

    private class UpdateTask extends TimerTask {
        private final Context context;
        private final AppWidgetManager appWidgetManager;
        private  int[] appWidgetIds;
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
            } catch (JSONException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void updateWidget() throws JSONException, InterruptedException {
            //Obtener review random
            getRandomReview();
            appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, PeliWidget.class));
            // Actualizar cada widget individualmente
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }

        private void getRandomReview() throws JSONException, InterruptedException {
            if(context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE).
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
                // Enqueue de manera síncrona usando BlockingCoroutineDispatcher

                try {
                    WorkManager workManager = WorkManager.getInstance();
                    workManager.enqueue(otwr).getResult().get();

                    // Esperar a que la tarea termine y obtener el resultado
                    //Como no es posible hacerlo de la manera "correcta"
                    //porque el context está detatched, se hará con thread
                    //para verificar que se ha realizado la petición correctamente
                    boolean finished = false;
                    while (!finished) {
                        WorkInfo workInfo = workManager.getWorkInfoById(otwr.getId()).get();
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            finished = true;
                            Data outputData = workInfo.getOutputData();
                            String res = outputData.getString("jsonResponse");

                            JSONArray listaPeliculas = new JSONArray(res);
                            int randomIndex = new Random().nextInt(listaPeliculas.length());

                            // Obtener el elemento JSONObject en el índice aleatorio
                            peli = listaPeliculas.getJSONObject(randomIndex);
                            peli.put("Error", false);
                        } else {
                            // Esperar antes de verificar de nuevo
                            Thread.sleep(1000); // Esperar 1 segundo a que finalice
                        }
                    }
                } catch (Exception e) {
                    peli = new JSONObject();
                    peli.put("Nombre","No hay películas");
                    peli.put("Error",true);
                    Drawable drawable = context.getResources().getDrawable(R.drawable._478111);
                    Bitmap bitmap = drawableToBitmap(drawable);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    peli.put("Imagen",bitmap);
                }
            }else {
                peli = new JSONObject();
                peli.put("Nombre","Inicia sesión");
                peli.put("Error",true);
                Drawable drawable = context.getResources().getDrawable(R.drawable._478111);
                Bitmap bitmap = drawableToBitmap(drawable);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                peli.put("Imagen",bitmap);

            }
        }

        private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) throws JSONException, InterruptedException {
            // Configurar las vistas del widget con la revisión obtenida
            if(!peli.getBoolean("Error")){
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.peli_widget);
                views.setTextViewText(R.id.nomWidget, peli.getString("Nombre"));
                GestorBD sgbd = new GestorBD(context);
                try {
                String base64 = sgbd.verImagen((String)peli.get("Imagen"));
                //String base64 = (String) peli.get("Imagen");
                    byte[] imagenBytes = Base64.getDecoder().decode(base64);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                    views.setImageViewBitmap(R.id.imgWidget, bitmap);
                }catch  (Exception e){
                    Drawable drawable = context.getResources().getDrawable(R.drawable._478111);
                    Bitmap bitmap = drawableToBitmap(drawable);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    views.setImageViewBitmap(R.id.imgWidget,bitmap);
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.peli_widget);
                views.setTextViewText(R.id.nomWidget,peli.getString("Nombre"));
                Drawable drawable = context.getResources().getDrawable(R.drawable._478111);
                Bitmap bitmap = drawableToBitmap(drawable);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                views.setImageViewBitmap(R.id.imgWidget,bitmap);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
        public Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        if (updateTimer == null) {
           // updateTimer = new Timer();
            // Programa la tarea de actualización para ejecutarse cada 30 segundos
           // updateTimer.scheduleAtFixedRate(new UpdateTask(context, appWidgetManager, appWidgetIds), 3000, 60000);// 60000 ms = 1 minuto
        }
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
        if (updateTimer == null) {
            updateTimer = new Timer();
            // Programa la tarea de actualización para ejecutarse cada 30 segundos
            updateTimer.scheduleAtFixedRate(new UpdateTask(context, appWidgetManager, appWidgetIds), 3000, 10000);// 60000 ms = 1 minuto
        } // 60000 ms = 1 minuto
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Este método se llama cuando se deshabilita el último widget
        // Cancela el temporizador si no hay más widgets activos
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_UPDATE_WIDGET.equals(intent.getAction())) {
            // Se recibió una acción para actualizar el widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, PeliWidget.class));

            if (updateTimer == null) {
                // Iniciar el temporizador si aún no está configurado
                updateTimer = new Timer();
                updateTimer.scheduleAtFixedRate(new UpdateTask(context, appWidgetManager, appWidgetIds), 0, 15000); // Cada 15 segundos
            }
        }
    }
}