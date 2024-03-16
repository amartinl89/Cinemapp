package com.example.cinemapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GestorBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "cinemapp.db";
    private static final int VERSION_BASE_DATOS = 1;

    public GestorBD(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla Review
        String queryCrearTabla = "CREATE TABLE IF NOT EXISTS Review (" +
                "Fecha DATETIME PRIMARY KEY, " +
                "Nombre VARCHAR(255), " +
                "Imagen BLOB, " +
                "Ano INTEGER, " +
                "Puntuacion INTEGER," +
                "Resena TEXT)";
        db.execSQL(queryCrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    public void insertarReview(String fecha, String nombre, byte[] imagen, int ano, String resena, int punt) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryInsertar = "INSERT INTO Review (Fecha, Nombre, Imagen, Ano, Resena, Puntuacion) VALUES (?, ?, ?, ?, ?, ?)";
        db.execSQL(queryInsertar, new Object[]{fecha, nombre, imagen, ano, resena, punt});
        db.close();
    }
        public void actualizarReview(String fecha, String nuevoNombre, byte[] nuevaImagen, int nuevoAno, String nuevaResena, int nuevaPunt) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("Nombre", nuevoNombre);
            values.put("Imagen", nuevaImagen);
            values.put("Ano", nuevoAno);
            values.put("Resena", nuevaResena);
            values.put("Puntuacion", nuevaPunt);

            String[] arg = new String[]{fecha};

            // Actualizar la fila basada en la columna 'Fecha'
            System.out.println(db.update("Review", values, "Fecha=?", arg));

            db.close();
    }
    public JSONArray visualizarLista() throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray js = new JSONArray();
        Cursor c = db.rawQuery("SELECT Fecha, Nombre, Imagen, Ano, Puntuacion, Resena FROM Review",null);
        while (c.moveToNext()){
            JSONObject j = new JSONObject();
            j.put("Fecha",c.getString(0));
            j.put("Nombre",c.getString(1));
            j.put("Imagen",c.getBlob(2));
            j.put("Ano",c.getInt(3));
            j.put("Puntuacion",c.getInt(4));
            j.put("Resena",c.getString(5));
            js.put(j);
        }
        c.close();
        return js;
    }

    public void borrarResena(String fecha){

        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println(db.delete("Review","Fecha=?", new String[]{fecha}));
        db.close();
    }
}
