package com.example.cinemapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    private final JSONArray listaPeliculas;
    private final String usuario;
    Context parent;

    public PeliculaAdapter(JSONArray listaPeliculas, String usuario) {
        this.listaPeliculas = listaPeliculas;
        this.usuario  = usuario;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
        parent = p.getContext();
        View view = LayoutInflater.from(parent).inflate(R.layout.ver_imagen, p, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        byte[] imagenBytes = null;
        try {
            JSONObject pelicula = listaPeliculas.getJSONObject(position);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String fecha = pelicula.getString("Fecha");
            String nom = pelicula.getString("Nombre");
            String ano = pelicula.getString("Ano");
            String punt = pelicula.getString("Puntuacion");
            // Configurar los elementos de la tarjeta con los datos de la base de datos
            holder.nombreTarjeta.setText(nom);
            holder.anoTarjeta.setText(ano);
            holder.puntTarjeta.setText(punt);
            GestorBD sgbd = new GestorBD(parent);
            try {
                String base64 = sgbd.verImagen((String) pelicula.get("Imagen"));
                imagenBytes = Base64.getDecoder().decode(base64);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                //String base64 = (String) pelicula.get("Imagen");
                //byte[] imagenBytes = (byte[]) pelicula.get("Imagen");
                //byte[] imagenBytes = Base64.getDecoder().decode(base64);
                System.out.println("Longitud de imagenBytes: " + imagenBytes.length);
                //Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                holder.imagenTarjeta.setImageBitmap(bitmap);
            }catch(Exception e){
                Drawable drawable = parent.getResources().getDrawable(R.drawable._478111);
                Bitmap bitmap = drawableToBitmap(drawable);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imagenBytes = stream.toByteArray();
                holder.imagenTarjeta.setImageBitmap(bitmap);
            }
            String resena = pelicula.getString("Resena");
            holder.verReview.setText(parent.getResources().getString(R.string.ver_detalle_str));
            byte[] finalImagenBytes = imagenBytes;
            holder.verReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Se guardan los datos de la reseña para la próxima vista
                    Intent intent = new Intent(parent, VisualizarReviewDetalle.class);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("nom", nom);
                    intent.putExtra("ano", ano);
                    intent.putExtra("punt", punt);
                    intent.putExtra("imagen", finalImagenBytes);
                    intent.putExtra("resena", resena);
                    intent.putExtra("nombre",usuario);
                    PeliculaAdapter.this.parent.startActivity(intent);
                }
            });
            holder.borrarResena.setText(parent.getResources().getString(R.string.delete_str));
            holder.borrarResena.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment popup = new PopUpBorrar();
                    Intent intent = new Intent(parent, VerPelicula.class);
                    intent.putExtra("fecha", fecha);
                    intent.putExtra("nombre",usuario);
                    ((PopUpBorrar) popup).setParentIntent(intent);
                    FragmentManager fragmentManager = ((AppCompatActivity) parent).getSupportFragmentManager();
                    popup.show(fragmentManager, "borrar");
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //Para poder usar imágenes de la app
    public static Bitmap drawableToBitmap(Drawable drawable) {
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

    @Override
    public int getItemCount() {return listaPeliculas.length(); }

    public static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenTarjeta;
        TextView nombreTarjeta;
        TextView anoTarjeta;
        TextView puntTarjeta;
        Button verReview;
        Button borrarResena;

        public PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenTarjeta = itemView.findViewById(R.id.imagenTarjeta);
            nombreTarjeta = itemView.findViewById(R.id.nombreTarjeta);
            anoTarjeta = itemView.findViewById(R.id.anoTarjeta);
            puntTarjeta = itemView.findViewById(R.id.puntTarjeta);
            verReview = itemView.findViewById(R.id.verReviewVerPeliculaV);
            borrarResena =itemView.findViewById(R.id.borarrVerPeliculaV);
        }

    }
}


