package com.example.ecolim;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.helpers.DBHelper;

public class Auth extends AppCompatActivity {

    EditText inputNombre, inputEmail, inputPassword;
    Spinner spRol;
    Button btnLogin, btnRegistro;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        dbHelper = new DBHelper(this);

        inputNombre = findViewById(R.id.inputNombre);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(v -> registrarUsuario());
        btnLogin.setOnClickListener(v -> iniciarSesion());
    }

    private void registrarUsuario() {
        String nombre = inputNombre.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if(nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("email", email);
        valores.put("password", password);
        valores.put("rol", "Usuario"); // Asignamos rol por defecto

        long resultado = db.insert(DBHelper.TABLA_USUARIO, null, valores);

        if(resultado != -1) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void iniciarSesion() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLA_USUARIO + " WHERE email=? AND password=?",
                new String[]{email, password});

        if(cursor.moveToFirst()) {
            int idUsuarioIndex = cursor.getColumnIndexOrThrow("idUsuario");
            int nombreIndex = cursor.getColumnIndexOrThrow("nombre");
            
            int idUsuario = cursor.getInt(idUsuarioIndex);
            String nombre = cursor.getString(nombreIndex);

            SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
            editor.putString("loggedUserEmail", email);
            editor.putInt("loggedUserId", idUsuario);
            editor.putString("loggedUserName", nombre);
            editor.apply();

            Toast.makeText(this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Inicio.class));
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
