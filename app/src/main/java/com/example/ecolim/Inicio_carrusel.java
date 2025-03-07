package com.example.ecolim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Inicio_carrusel extends RecyclerView.Adapter<Inicio_carrusel.CarruselViewHolder> {

    Context context;
    List<Integer> listaImagenes;
    List<String> listaTitulos;
    List<String> listaSubtitulos;

    public Inicio_carrusel(Context context, List<Integer> listaImagenes, List<String> titulos, List<String> subtitulos) {
        this.context = context;
        this.listaImagenes = listaImagenes;
        this.listaTitulos = titulos;
        this.listaSubtitulos = subtitulos;
    }

    @NonNull
    @Override
    public CarruselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_inicio_carrusel, parent, false);
        return new CarruselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarruselViewHolder holder, int position) {
        holder.imagen.setImageResource(listaImagenes.get(position));
        holder.titulo.setText(listaTitulos.get(position));
        holder.subtitulo.setText(listaSubtitulos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    static class CarruselViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo, subtitulo;

        CarruselViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageCarrusel);
            titulo = itemView.findViewById(R.id.tituloImagen);
            subtitulo = itemView.findViewById(R.id.subtituloImagen);
        }
    }
}
