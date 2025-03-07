package com.example.ecolim;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Toolbar extends AppCompatActivity {

    protected View toolbar;

    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_inicio) {
            Toast.makeText(this, "Se presionó el icono de Inicio", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_conoceme) {
            Toast.makeText(this, "Se presionó el icono de Conoceme", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_proyectos) {
            Toast.makeText(this, "Se presionó la opción de Proyectos", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_otros) {
            Toast.makeText(this, "Se presionó la opción de Otros recursos", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_configuracion) {
            Toast.makeText(this, "Se presionó la opción de Configuración", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_salir) {
            Toast.makeText(this, "Se presionó la opción de Salir", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}