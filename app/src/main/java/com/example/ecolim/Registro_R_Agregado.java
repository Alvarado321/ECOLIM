package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.example.ecolim.helpers.ResiduoDAO;
import com.example.ecolim.menu.BaseActivity;

import java.util.List;
import java.util.Arrays;

public class Registro_R_Agregado extends BaseActivity {

    Spinner spinnerEmpleado, spinnerTipoResiduo;
    EditText editTextCantidad, editObservaciones;
    TextView textFechaHora;
    Button btnRegistrar;
    ImageView imgTipoResiduo;
    ResiduoDAO residuoDAO;

    String usuarioAutenticado = "Empleado actual";

    List<String> tiposResiduo = Arrays.asList("Plástico", "Vidrio", "Metal", "Orgánico", "Papel", "Electrónico");
    int[] imagenesResiduo = {
            R.drawable.registro_residuo_img1, R.drawable.registro_residuo_img2, R.drawable.registro_residuo_img3,
            R.drawable.registro_residuo_img4, R.drawable.registro_residuo_img5, R.drawable.registro_residuo_img6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_r_agregado);
        configurarToolbar("Agregado de Residuos", true);

        spinnerEmpleado = findViewById(R.id.spinnerEmpleado);
        spinnerTipoResiduo = findViewById(R.id.spinnerTipoResiduo);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        editObservaciones = findViewById(R.id.editObservaciones);
        textFechaHora = findViewById(R.id.textFechaHora);
        btnRegistrar = findViewById(R.id.buttonRegistrarResiduo);
        imgTipoResiduo = findViewById(R.id.imgTipoResiduo);

        residuoDAO = new ResiduoDAO(this);

        cargarEmpleados();

        spinnerTipoResiduo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tiposResiduo));
        spinnerTipoResiduo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                imgTipoResiduo.setImageResource(imagenesResiduo[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        String fechaHoraActual = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        textFechaHora.setText(fechaHoraActual);

        btnRegistrar.setOnClickListener(v -> registrarResiduoEnDB());
    }

    void cargarEmpleados(){
        List<String> empleados = residuoDAO.obtenerListaNombresEmpleados();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, empleados);
        spinnerEmpleado.setAdapter(adapter);
        spinnerEmpleado.setSelection(adapter.getPosition(usuarioAutenticado));
    }

    void registrarResiduoEnDB() {
        if(editTextCantidad.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        String empleado = spinnerEmpleado.getSelectedItem().toString();
        String tipoResiduo = spinnerTipoResiduo.getSelectedItem().toString();
        double cantidadResiduo = Double.parseDouble(editTextCantidad.getText().toString());
        String observaciones = editObservaciones.getText().toString();
        String fechaHora = textFechaHora.getText().toString();

        boolean exito = residuoDAO.insertarRegistroResiduo(
                empleado, tipoResiduo, cantidadResiduo, observaciones, fechaHora);

        Toast.makeText(this, exito ? "Registrado correctamente" : "Error al registrar", Toast.LENGTH_SHORT).show();
        if(exito) startActivity(new Intent(this, Registro_R_Agregado.class));
    }
}
