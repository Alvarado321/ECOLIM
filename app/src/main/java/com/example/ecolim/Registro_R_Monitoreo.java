package com.example.ecolim;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.adapters.RegistroResiduoAdapter;
import com.example.ecolim.helpers.ResiduoDAO;
import com.example.ecolim.menu.BaseActivity;
import com.example.ecolim.models.RegistroResiduo;
import java.util.ArrayList;
import java.util.List;

public class Registro_R_Monitoreo extends BaseActivity {

    RecyclerView recyclerView;
    RegistroResiduoAdapter adapter;
    ResiduoDAO residuoDAO;
    private SearchView etBuscar;
    TextView txtTotalRegistros, txtCantidadTotal, txtResiduoComun;

    List<RegistroResiduo> listaRegistros;
    List<RegistroResiduo> listaFiltrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_r_monitoreo);
        configurarToolbar("Monitoreo y Gestión", true);

        recyclerView = findViewById(R.id.recyclerMonitoreo);
        txtTotalRegistros = findViewById(R.id.txtTotalRegistros);
        txtCantidadTotal = findViewById(R.id.txtCantidadTotal);
        txtResiduoComun = findViewById(R.id.txtResiduoComun);
        etBuscar = findViewById(R.id.etbuscar);

        residuoDAO = new ResiduoDAO(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarDatos();

        etBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarDatos(newText);
                return true;
            }
        });
    }

    void cargarDatos() {
        int total = residuoDAO.obtenerTotalRegistros();
        double cantidadTotal = residuoDAO.obtenerCantidadTotal();
        String masComun = residuoDAO.obtenerResiduoMasComun();

        txtTotalRegistros.setText(String.valueOf(total));
        txtCantidadTotal.setText(String.valueOf((int) cantidadTotal));
        txtResiduoComun.setText(masComun);

        listaRegistros = residuoDAO.obtenerTodosRegistros();
        listaFiltrada = new ArrayList<>(listaRegistros);

        adapter = new RegistroResiduoAdapter(listaFiltrada);
        recyclerView.setAdapter(adapter);
    }

    void filtrarDatos(String query) {
        List<RegistroResiduo> listaTemporal = new ArrayList<>();
        if (query.isEmpty()) {
            listaTemporal.addAll(listaRegistros);
        } else {
            for (RegistroResiduo registro : listaRegistros) {
                if (registro.empleado.toLowerCase().contains(query.toLowerCase()) ||
                        registro.tipoResiduo.toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(registro.cantidad).contains(query) ||
                        registro.fechaRegistro.toLowerCase().contains(query.toLowerCase()) ||
                        registro.observaciones.toLowerCase().contains(query.toLowerCase())) {
                    listaTemporal.add(registro);
                }
            }
        }
        adapter.setData(listaTemporal);
    }
}
