package com.example.ecolim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.helpers.DBHelper;

public class Auth extends AppCompatActivity {

    EditText inputNombre, inputEmail, inputPassword;
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

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("email", email);
        valores.put("password", password);

        long resultado = db.insert(DBHelper.TABLA_EMPLEADO, null, valores);

        if(resultado != -1)
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "El email ya existe o hubo un error", Toast.LENGTH_SHORT).show();

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
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLA_EMPLEADO + " WHERE email=? AND password=?",
                new String[]{email, password});

        if(cursor.moveToFirst()) {
            Toast.makeText(this, "Inicio exitoso", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Inicio.class));
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
