package com.example.ecolim;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.adapters.UsuariosAdapter;
import com.example.ecolim.api.ApiClient;
import com.example.ecolim.menu.BaseActivity;
import com.example.ecolim.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionUsuarios extends BaseActivity {
    private EditText etNombre, etEmail, etPassword;
    private Spinner spRol;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvUsuarios;
    private UsuariosAdapter adapter;
    private List<Usuario> listaUsuarios;
    private Integer usuarioEditandoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);
        configurarToolbar("Gestion de Usuarios", true);

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spRol = findViewById(R.id.spRol);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        rvUsuarios = findViewById(R.id.rvUsuarios);

        listaUsuarios = new ArrayList<>();
        adapter = new UsuariosAdapter(this, listaUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        rvUsuarios.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardarUsuario());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        adapter.setOnUsuarioListener(new UsuariosAdapter.OnUsuarioListener() {
            @Override
            public void onEditClick(Usuario usuario) {
                editarUsuario(usuario);
            }

            @Override
            public void onDeleteClick(Usuario usuario) {
                confirmarEliminarUsuario(usuario);
            }
        });

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        ApiClient.getApiService().obtenerUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.clear();
                    listaUsuarios.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GestionUsuarios.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(GestionUsuarios.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rol = spRol.getSelectedItem().toString();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRol(rol);

        if (usuarioEditandoId == null) {
            ApiClient.getApiService().registrarUsuario(usuario).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(GestionUsuarios.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        cargarUsuarios();
                    } else {
                        Toast.makeText(GestionUsuarios.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(GestionUsuarios.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            usuario.setIdUsuario(usuarioEditandoId);
            ApiClient.getApiService().actualizarUsuario(usuarioEditandoId, usuario).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(GestionUsuarios.this, "Usuario actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        cargarUsuarios();
                    } else {
                        Toast.makeText(GestionUsuarios.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(GestionUsuarios.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void editarUsuario(Usuario usuario) {
        usuarioEditandoId = usuario.getIdUsuario();
        etNombre.setText(usuario.getNombre());
        etEmail.setText(usuario.getEmail());
        etPassword.setText(usuario.getPassword());
        String[] roles = getResources().getStringArray(R.array.roles_usuario);
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(usuario.getRol())) {
                spRol.setSelection(i);
                break;
            }
        }
        btnGuardar.setText("Actualizar");
    }

    private void confirmarEliminarUsuario(Usuario usuario) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
            .setPositiveButton("Sí", (dialog, which) -> {
                ApiClient.getApiService().eliminarUsuario(usuario.getIdUsuario()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GestionUsuarios.this, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                            cargarUsuarios();
                        } else {
                            Toast.makeText(GestionUsuarios.this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(GestionUsuarios.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etEmail.setText("");
        etPassword.setText("");
        spRol.setSelection(0);
        usuarioEditandoId = null;
        btnGuardar.setText("Guardar");
    }
}
