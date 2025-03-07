package com.example.ecolim.menu;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ecolim.Auth;
import com.example.ecolim.Inicio;
import com.example.ecolim.R;
import com.example.ecolim.Registro_R_Agregado;
import com.example.ecolim.Registro_R_Monitoreo;
import com.example.ecolim.Registro_R_Reportes;

public abstract class BaseActivity extends AppCompatActivity {

    protected void configurarToolbar(String titulo, boolean mostrarBotonAtras) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titulo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mostrarBotonAtras);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
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
            startActivity(new Intent(this, Inicio.class));
            return true;
        } else if (id == R.id.action_agregar) {
            startActivity(new Intent(this, Registro_R_Agregado.class));
            return true;
        } else if (id == R.id.action_listar) {
            startActivity(new Intent(this, Registro_R_Monitoreo.class));
            return true;
        } else if (id == R.id.action_reportes) {
            startActivity(new Intent(this, Registro_R_Reportes.class));
            return true;
        } else if (id == R.id.action_conoceme) {
            Toast.makeText(this, "Se presionó el icono de Conóceme", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_proyectos) {
            Toast.makeText(this, "Se presionó la opción de Proyectos", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_configuracion) {
            Toast.makeText(this, "Se presionó la opción de Configuración", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_salir) {
            startActivity(new Intent(this, Auth.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
