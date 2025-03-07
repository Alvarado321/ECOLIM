package com.example.ecolim;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.ecolim.helpers.DBHelper;
import com.example.ecolim.menu.BaseActivity;

public class Configuracion extends BaseActivity {
    private Button btnActualizar;
    private EditText etNombre, etCorreo, etContrasena;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        configurarToolbar("Configuración de perfil", true);

        btnActualizar = findViewById(R.id.buttonActualizar);
        etNombre = findViewById(R.id.et_nombre);
        etCorreo = findViewById(R.id.et_correo);
        etContrasena = findViewById(R.id.et_contrasena);

        mostrarDatosUsuarioLogueado();

        btnActualizar.setOnClickListener(v -> actualizarDatosUsuario());
    }

    private void actualizarDatosUsuario() {
        String nuevoNombre = etNombre.getText().toString();
        String nuevaContrasena = etContrasena.getText().toString();

        SQLiteDatabase db = null;
        try {
            db = new DBHelper(this).getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("nombre", nuevoNombre);
            valores.put("password", nuevaContrasena);

            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            String loggedEmail = prefs.getString("loggedUserEmail", "");

            int filasAfectadas = db.update(
                    DBHelper.TABLA_EMPLEADO,
                    valores,
                    "email = ?",
                    new String[]{loggedEmail}
            );

            if (filasAfectadas > 0) {
                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DB_UPDATE", "Error: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
    }

    private void mostrarDatosUsuarioLogueado() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String loggedEmail = prefs.getString("loggedUserEmail", null);

        if (loggedEmail == null) {
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = new DBHelper(this).getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT nombre, email, password FROM " + DBHelper.TABLA_EMPLEADO + " WHERE email = ?",
                    new String[]{loggedEmail}
            );

            if (cursor.moveToFirst()) {
                etNombre.setText(cursor.getString(0));
                etCorreo.setText(cursor.getString(1));
                etContrasena.setText(cursor.getString(2));
            } else {
                Toast.makeText(this, "Error: Cuenta no encontrada", Toast.LENGTH_LONG).show();
                cerrarSesion();
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    private void cerrarSesion() {
        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.remove("loggedUserEmail");
        editor.apply();

        Intent intent = new Intent(this, Auth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}