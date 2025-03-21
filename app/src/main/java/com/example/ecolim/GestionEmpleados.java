package com.example.ecolim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.adapters.EmpleadosAdapter;
import com.example.ecolim.helpers.DBHelper;
import com.example.ecolim.menu.BaseActivity;
import com.example.ecolim.models.EmpleadoModel;
import java.util.ArrayList;
import java.util.List;

public class GestionEmpleados extends BaseActivity implements EmpleadosAdapter.OnEmpleadoListener {
    private EditText etNombre, etEmail, etCargo, etDepartamento;
    private Button btnGuardar, btnLimpiar;
    private RecyclerView rvEmpleados;
    private EmpleadosAdapter adapter;
    private List<EmpleadoModel> listaEmpleados;
    private DBHelper dbHelper;
    private Integer empleadoEditandoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_empleados);
        configurarToolbar("Gestion de Empleados", true);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etCargo = findViewById(R.id.etCargo);
        etDepartamento = findViewById(R.id.etDepartamento);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        rvEmpleados = findViewById(R.id.rvEmpleados);

        dbHelper = new DBHelper(this);
        listaEmpleados = new ArrayList<>();
        adapter = new EmpleadosAdapter(this, listaEmpleados);
        adapter.setOnEmpleadoListener(this);

        rvEmpleados.setLayoutManager(new LinearLayoutManager(this));
        rvEmpleados.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardarEmpleado());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        cargarEmpleados();
    }

    private void cargarEmpleados() {
        listaEmpleados.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLA_EMPLEADO, null, null, null, null, null, "nombre ASC");

        int idEmpleadoIndex = cursor.getColumnIndexOrThrow("idEmpleado");
        int nombreIndex = cursor.getColumnIndexOrThrow("nombre");
        int emailIndex = cursor.getColumnIndexOrThrow("email");
        int cargoIndex = cursor.getColumnIndexOrThrow("cargo");
        int departamentoIndex = cursor.getColumnIndexOrThrow("departamento");
        int activoIndex = cursor.getColumnIndexOrThrow("activo");

        while (cursor.moveToNext()) {
            EmpleadoModel empleado = new EmpleadoModel();
            empleado.setIdEmpleado(cursor.getInt(idEmpleadoIndex));
            empleado.setNombre(cursor.getString(nombreIndex));
            empleado.setEmail(cursor.getString(emailIndex));
            empleado.setCargo(cursor.getString(cargoIndex));
            empleado.setDepartamento(cursor.getString(departamentoIndex));
            empleado.setActivo(cursor.getInt(activoIndex) == 1);
            listaEmpleados.add(empleado);
        }

        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }

    private void guardarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String cargo = etCargo.getText().toString().trim();
        String departamento = etDepartamento.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || cargo.isEmpty() || departamento.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("email", email);
        valores.put("cargo", cargo);
        valores.put("departamento", departamento);

        long resultado;
        if (empleadoEditandoId == null) {
            // Insertar nuevo empleado
            valores.put("activo", 1); // Por defecto activo
            resultado = db.insert(DBHelper.TABLA_EMPLEADO, null, valores);
        } else {
            // Actualizar empleado existente
            resultado = db.update(DBHelper.TABLA_EMPLEADO, valores, 
                "idEmpleado = ?", new String[]{empleadoEditandoId.toString()});
        }

        db.close();

        if (resultado != -1) {
            Toast.makeText(this, empleadoEditandoId == null ? 
                "Empleado registrado exitosamente" : "Empleado actualizado exitosamente", 
                Toast.LENGTH_SHORT).show();
            limpiarCampos();
            cargarEmpleados();
        } else {
            Toast.makeText(this, "Error al guardar empleado", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etEmail.setText("");
        etCargo.setText("");
        etDepartamento.setText("");
        empleadoEditandoId = null;
        btnGuardar.setText("Guardar");
    }

    @Override
    public void onEditClick(EmpleadoModel empleado) {
        empleadoEditandoId = empleado.getIdEmpleado();
        etNombre.setText(empleado.getNombre());
        etEmail.setText(empleado.getEmail());
        etCargo.setText(empleado.getCargo());
        etDepartamento.setText(empleado.getDepartamento());
        btnGuardar.setText("Actualizar");
    }

    @Override
    public void onDeleteClick(EmpleadoModel empleado) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este empleado?")
            .setPositiveButton("Sí", (dialog, which) -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int resultado = db.delete(DBHelper.TABLA_EMPLEADO, 
                    "idEmpleado = ?", new String[]{String.valueOf(empleado.getIdEmpleado())});
                db.close();

                if (resultado > 0) {
                    Toast.makeText(this, "Empleado eliminado exitosamente", 
                        Toast.LENGTH_SHORT).show();
                    cargarEmpleados();
                } else {
                    Toast.makeText(this, "Error al eliminar empleado", 
                        Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    @Override
    public void onToggleActivoClick(EmpleadoModel empleado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("activo", empleado.isActivo() ? 0 : 1);

        int resultado = db.update(DBHelper.TABLA_EMPLEADO, valores,
            "idEmpleado = ?", new String[]{String.valueOf(empleado.getIdEmpleado())});
        db.close();

        if (resultado > 0) {
            empleado.setActivo(!empleado.isActivo());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, 
                empleado.isActivo() ? "Empleado activado" : "Empleado desactivado", 
                Toast.LENGTH_SHORT).show();
        }
    }
}
