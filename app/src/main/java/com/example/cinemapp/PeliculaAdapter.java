package com.example.cinemapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    private final JSONArray listaPeliculas;

    public PeliculaAdapter(JSONArray listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_imagen, parent, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        try {
            JSONObject pelicula = listaPeliculas.getJSONObject(position);

            // Configurar los elementos de la tarjeta con los datos de la base de datos
            holder.nombreTarjeta.setText(pelicula.getString("Nombre"));
            holder.anoTarjeta.setText(pelicula.getString("Ano"));
            holder.puntTarjeta.setText(pelicula.getString("Puntuacion"));
            // Aquí puedes configurar otros elementos de la tarjeta, como la imagen

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaPeliculas.length();
    }

    public static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenTarjeta;
        TextView nombreTarjeta;
        TextView anoTarjeta;
        TextView puntTarjeta;

        public PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenTarjeta = itemView.findViewById(R.id.imagenTarjeta);
            nombreTarjeta = itemView.findViewById(R.id.nombreTarjeta);
            anoTarjeta = itemView.findViewById(R.id.anoTarjeta);
            puntTarjeta = itemView.findViewById(R.id.puntTarjeta);
            // Configura otros elementos de la tarjeta si es necesario
        }
    }
}


