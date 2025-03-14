package com.example.ecolim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.adapters.UsuariosAdapter;
import com.example.ecolim.helpers.DBHelper;
import com.example.ecolim.menu.BaseActivity;
import com.example.ecolim.models.Usuario;
import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios extends BaseActivity {
    private EditText etNombre, etEmail, etPassword;
    private Spinner spRol;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvUsuarios;
    private UsuariosAdapter adapter;
    private List<Usuario> listaUsuarios;
    private DBHelper dbHelper;
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

        dbHelper = new DBHelper(this);

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
        listaUsuarios.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLA_USUARIO, null, null, null, null, null, "nombre ASC");

        int idUsuarioIndex = cursor.getColumnIndexOrThrow("idUsuario");
        int nombreIndex = cursor.getColumnIndexOrThrow("nombre");
        int emailIndex = cursor.getColumnIndexOrThrow("email");
        int passwordIndex = cursor.getColumnIndexOrThrow("password");
        int rolIndex = cursor.getColumnIndexOrThrow("rol");

        while (cursor.moveToNext()) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(cursor.getInt(idUsuarioIndex));
            usuario.setNombre(cursor.getString(nombreIndex));
            usuario.setEmail(cursor.getString(emailIndex));
            usuario.setPassword(cursor.getString(passwordIndex));
            usuario.setRol(cursor.getString(rolIndex));
            listaUsuarios.add(usuario);
        }

        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
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

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("email", email);
        valores.put("password", password);
        valores.put("rol", rol);

        long resultado;
        if (usuarioEditandoId == null) {
            resultado = db.insert(DBHelper.TABLA_USUARIO, null, valores);
        } else {
            resultado = db.update(DBHelper.TABLA_USUARIO, valores,
                "idUsuario = ?", new String[]{usuarioEditandoId.toString()});
        }

        db.close();

        if (resultado != -1) {
            Toast.makeText(this, usuarioEditandoId == null ?
                "Usuario registrado exitosamente" : "Usuario actualizado exitosamente",
                Toast.LENGTH_SHORT).show();
            limpiarCampos();
            cargarUsuarios();
        } else {
            Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarUsuario(Usuario usuario) {
        usuarioEditandoId = usuario.getIdUsuario();
        etNombre.setText(usuario.getNombre());
        etEmail.setText(usuario.getEmail());
        etPassword.setText(usuario.getPassword());
        // Encontrar y seleccionar el rol en el spinner
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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int resultado = db.delete(DBHelper.TABLA_USUARIO,
                    "idUsuario = ?", new String[]{String.valueOf(usuario.getIdUsuario())});
                db.close();

                if (resultado > 0) {
                    Toast.makeText(this, "Usuario eliminado exitosamente",
                        Toast.LENGTH_SHORT).show();
                    cargarUsuarios();
                } else {
                    Toast.makeText(this, "Error al eliminar usuario",
                        Toast.LENGTH_SHORT).show();
                }
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
