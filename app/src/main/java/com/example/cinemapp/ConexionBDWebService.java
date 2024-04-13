package com.example.cinemapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexionBDWebService extends Worker {

    private static final String TAG = "ConexionBDWebService";
    private static final String BASE_URL = "http://10.0.2.2:81/api.php";

    public ConexionBDWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String operation = getInputData().getString("operation");
        try {
        switch (operation) {

            case "insertarReview":
                return insertarReview();
            case "actualizarReview":
                return actualizarReview();
            case "borrarResena":
                return borrarResena();
            case "insertarUsuario":
                return insertarUsuario();
            case "obtenerUsuario":
                    return obtenerUsuario();
            case "visualizarLista":
                return visualizarLista();
            default:
                Log.e(TAG, "Operación no válida: " + operation);
                return Result.failure();
        }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Result insertarReview() {
        String fecha = getInputData().getString("fecha");
        String usuario = getInputData().getString("usuario");
        String nombre = getInputData().getString("nombre");
        String imagen = getInputData().getString("imagen");
        int ano = getInputData().getInt("ano", 0);
        String resena = getInputData().getString("resena");
        int punt = getInputData().getInt("punt", 0);

        try {
            String apiUrl = BASE_URL + "?operation=insertarReview";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Construir los parámetros para la solicitud POST
            String postData = "fecha=" + fecha + "&usuario=" + usuario + "&nombre=" + nombre +
                    "&imagen=" + imagen + "&ano=" + ano + "&resena=" + resena + "&punt=" + punt;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Escribir los parámetros en la conexión
            connection.getOutputStream().write(postDataBytes);

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            // Analizar la respuesta del servidor
            JSONObject jsonResponse = new JSONObject(response.toString());
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar revisión: " + e.getMessage());
            return Result.failure();
        }
    }


    private Result actualizarReview() {
        String fecha = getInputData().getString("fecha");
        String nuevoNombre = getInputData().getString("nuevoNombre");
        String nuevaImagen = getInputData().getString("nuevaImagen");
        int nuevoAno = getInputData().getInt("nuevoAno", 0);
        String nuevaResena = getInputData().getString("nuevaResena");
        int nuevaPunt = getInputData().getInt("nuevaPunt", 0);

        try {
            String apiUrl = BASE_URL + "?operation=actualizarReview";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Construir los parámetros para la solicitud POST
            String postData = "fecha=" + fecha + "&nuevoNombre=" + nuevoNombre +
                    "&nuevaImagen=" + nuevaImagen + "&nuevoAno=" + nuevoAno +
                    "&nuevaResena=" + nuevaResena + "&nuevaPunt=" + nuevaPunt;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Escribir los parámetros en la conexión
            connection.getOutputStream().write(postDataBytes);

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            // Analizar la respuesta del servidor
            JSONObject jsonResponse = new JSONObject(response.toString());
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar revisión: " + e.getMessage());
            return Result.failure();
        }
    }


    private Result borrarResena() {
        String fecha = getInputData().getString("fecha");

        try {
            String apiUrl = BASE_URL + "?operation=borrarResena";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Construir los parámetros para la solicitud POST
            String postData = "fecha=" + fecha;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Escribir los parámetros en la conexión
            connection.getOutputStream().write(postDataBytes);

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            // Analizar la respuesta del servidor
            JSONObject jsonResponse = new JSONObject(response.toString());
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al borrar reseña: " + e.getMessage());
            return Result.failure();
        }
    }


    private Result insertarUsuario() {
        String nombre = getInputData().getString("nombre");
        String contrasena = getInputData().getString("contrasena");

        try {
            String apiUrl = BASE_URL + "?operation=insertarUsuario";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Construir los parámetros para la solicitud POST
            String postData = "nombre=" + nombre + "&contrasena=" + contrasena;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            // Escribir los parámetros en la conexión
            connection.getOutputStream().write(postDataBytes);

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            // Analizar la respuesta del servidor
            JSONObject jsonResponse = new JSONObject(response.toString());
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar usuario: " + e.getMessage());
            return Result.failure();
        }
    }

    private Result obtenerUsuario() throws JSONException {
        String nombre = getInputData().getString("nombre");
        String contrasena = getInputData().getString("contrasena");
        JSONObject j = new JSONObject();
        j.put("nombre", nombre);
        j.put("contrasena", contrasena);
        j.put("operation","obtenerUsuario");

        try {
            //String apiUrl = BASE_URL + "?operation=obtenerUsuario";
            String response = ApiCliente.executePost(BASE_URL,j.toString());

            if (response != null) {
                Data outputData = new Data.Builder()
                        .putString("jsonResponse", response)
                        .build();
                return Result.success(outputData);
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener usuario: " + e.getMessage());
            return Result.failure();
        }
    }

    private Result visualizarLista() {
        String usuario = getInputData().getString("usuario");

        try {
            String apiUrl = BASE_URL + "?operation=visualizarLista&usuario=" + usuario;
            String response = executeHttpRequest(apiUrl);

            if (response != null) {
                Data outputData = new Data.Builder()
                        .putString("jsonResponse", response)
                        .build();
                return Result.success(outputData);
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener lista de revisiones: " + e.getMessage());
            return Result.failure();
        }
    }

    @NonNull
    private String executeHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        connection.disconnect();
        return response.toString().trim();
    }
}
