package com.example.ecolim;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.helpers.ResiduoDAO;
import com.example.ecolim.menu.BaseActivity;
import com.example.ecolim.models.ReporteResiduo;
import com.example.ecolim.adapters.ReporteResiduoAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Registro_R_Reportes extends BaseActivity {

    private RecyclerView recyclerView;
    private BarChart barChart;
    private Button btnFechaInicio, btnFechaFin;
    private TextView txtFechaInicio, txtFechaFin;
    private ResiduoDAO residuoDAO;
    private ReporteResiduoAdapter adapter;
    private String fechaInicio, fechaFin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_r_reportes);
        configurarToolbar("Reportes de Residuo", true);

        residuoDAO = new ResiduoDAO(this);

        recyclerView = findViewById(R.id.recyclerReportes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReporteResiduoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFin);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        barChart = findViewById(R.id.barChart);

        btnFechaInicio.setOnClickListener(v -> seleccionarFecha(true));
        btnFechaFin.setOnClickListener(v -> seleccionarFecha(false));

        cargarReporteInicial();
    }

    void cargarReporteInicial() {
        List<ReporteResiduo> lista = residuoDAO.obtenerReportePorTipo();
        actualizarReporte(lista);
    }

    void seleccionarFecha(boolean esFechaInicio) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            if (esFechaInicio) {
                fechaInicio = dateFormat.format(calendar.getTime());
                txtFechaInicio.setText("Inicio: " + fechaInicio);
            } else {
                fechaFin = dateFormat.format(calendar.getTime());
                txtFechaFin.setText("Fin: " + fechaFin);
            }
            validarYGenerarReporte();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    void validarYGenerarReporte() {
        if (fechaInicio != null && fechaFin != null) {
            cargarReportePorFechas();
        }
    }

    void cargarReportePorFechas() {
        List<ReporteResiduo> lista = residuoDAO.obtenerReportesPorFecha(fechaInicio, fechaFin);
        actualizarReporte(lista);
    }

    void actualizarReporte(List<ReporteResiduo> lista) {
        adapter.actualizarDatos(lista);
        cargarGrafico(lista);
    }

    void cargarGrafico(List<ReporteResiduo> lista) {
        barChart.clear();

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (ReporteResiduo item : lista) {
            entries.add(new BarEntry(index++, (float) item.cantidadTotal));
            labels.add(item.tipoResiduo);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Residuos por tipo");
        BarData data = new BarData(dataSet);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.getDescription().setText("Reporte de Residuos");
        barChart.getDescription().setTextSize(12f);

        barChart.setData(data);
        barChart.animateY(1500);
        barChart.invalidate();
    }
}
