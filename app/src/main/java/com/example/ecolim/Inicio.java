package com.example.ecolim;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Arrays;
import java.util.List;

public class Inicio extends AppCompatActivity {

    ViewPager2 viewPagerImagenes;
    LinearLayout layoutPuntos;
    Inicio_carrusel adapter;

    List<Integer> imagenes = Arrays.asList(R.drawable.inicio_img1, R.drawable.inicio_img1, R.drawable.inicio_img1);
    List<String> titulos = Arrays.asList("Recicla con ECOLIM", "Gestiona eficientemente", "Cuidado Ambiental");
    List<String> subtitulos = Arrays.asList("Ayudamos al planeta", "Monitoreo en tiempo real", "Responsabilidad ecológica");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        viewPagerImagenes = findViewById(R.id.viewPagerImagenes);
        layoutPuntos = findViewById(R.id.layoutPuntos);

        adapter = new Inicio_carrusel(this, imagenes, titulos, subtitulos);
        viewPagerImagenes.setAdapter(adapter);

        configurarIndicadores();

        viewPagerImagenes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                actualizarIndicadores(position);
            }
        });
    }

    void configurarIndicadores() {
        ImageView[] puntos = new ImageView[adapter.getItemCount()];
        layoutPuntos.removeAllViews();

        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = new ImageView(this);
            puntos[i].setImageDrawable(ContextCompat.getDrawable(this,
                    i == 0 ? R.drawable.inicio_punto_activo : R.drawable.inicio_punto_inactivo));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            layoutPuntos.addView(puntos[i], params);
        }
    }

    void actualizarIndicadores(int posicion) {
        int total = layoutPuntos.getChildCount();
        for (int i = 0; i < total; i++) {
            ImageView imageView = (ImageView) layoutPuntos.getChildAt(i);
            if (i == posicion) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inicio_punto_activo));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inicio_punto_inactivo));
            }
        }
    }
}
