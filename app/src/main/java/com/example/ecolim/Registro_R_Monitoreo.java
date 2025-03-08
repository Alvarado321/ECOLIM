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

import java.util.List;

public class Registro_R_Monitoreo extends BaseActivity {

    RecyclerView recyclerView;
    RegistroResiduoAdapter adapter;
    ResiduoDAO residuoDAO;
    private SearchView etBuscar;
    TextView txtTotalRegistros, txtCantidadTotal, txtResiduoComun;

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
    }

    void cargarDatos() {
        int total = residuoDAO.obtenerTotalRegistros();
        double cantidadTotal = residuoDAO.obtenerCantidadTotal();
        String masComun = residuoDAO.obtenerResiduoMasComun();

        txtTotalRegistros.setText(total);
        txtCantidadTotal.setText((int) cantidadTotal);
        txtResiduoComun.setText(masComun);

        List<RegistroResiduo> lista = residuoDAO.obtenerTodosRegistros();
        adapter = new RegistroResiduoAdapter(lista);
        recyclerView.setAdapter(adapter);
    }
}
