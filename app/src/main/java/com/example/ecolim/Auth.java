package com.example.ecolim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecolim.api.ApiClient;
import com.example.ecolim.api.requests.LoginRequest;
import com.example.ecolim.api.responses.LoginResponse;
import com.example.ecolim.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {

    EditText inputNombre, inputEmail, inputPassword;
    Spinner spRol;
    Button btnLogin, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setRol("Usuario"); // Rol por defecto

        ApiClient.getApiService().registrarUsuario(nuevoUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Auth.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(Auth.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(Auth.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarSesion() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);
        ApiClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Usuario usuario = response.body().getData();

                    SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                    editor.putString("loggedUserEmail", email);
                    editor.putInt("loggedUserId", usuario.getIdUsuario());
                    editor.putString("loggedUserName", usuario.getNombre());
                    editor.apply();

                    Toast.makeText(Auth.this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Auth.this, Inicio.class));
                    finish();
                } else {
                    Toast.makeText(Auth.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Auth.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        inputNombre.setText("");
        inputEmail.setText("");
        inputPassword.setText("");
    }
}
