package com.example.cinemapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
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
                "Resena TEXT)";
        db.execSQL(queryCrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualización de la base de datos (si es necesario en versiones futuras)
        // Aquí puedes realizar tareas como agregar nuevas columnas o realizar migraciones de datos.
    }

    // Métodos adicionales para interactuar con la base de datos (por ejemplo, insertar, actualizar, consultar)

    // Ejemplo de método para insertar una nueva revisión en la tabla
    public void insertarReview(Date fecha, String nombre, byte[] imagen, int ano, String resena) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryInsertar = "INSERT INTO Review (Fecha, Nombre, Imagen, Ano, Resena) VALUES (?, ?, ?, ?, ?)";
        db.execSQL(queryInsertar, new Object[]{fecha, nombre, imagen, ano, resena});
        db.close();
    }
    public void actualizarReview(Date fecha, String nuevoNombre, byte[] nuevaImagen, int nuevoAno, String nuevaResena) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Nombre", nuevoNombre);
        values.put("Imagen", nuevaImagen);
        values.put("Ano", nuevoAno);
        values.put("Resena", nuevaResena);

        // Convertir la fecha a un formato de cadena que coincida con el formato en la base de datos
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaString = sdf.format(fecha);

        // Actualizar la fila basada en la columna 'Fecha'
        db.update("Review", values, "Fecha=?", new String[]{fechaString});

        db.close();
    }

}
