package com.example.ecolim;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Button btnExportarPDF;

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

        btnExportarPDF = findViewById(R.id.btnExportarPDF);
        btnExportarPDF.setOnClickListener(v -> exportarReportePDF());
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

    private void exportarReportePDF() {
        List<ReporteResiduo> datos;

        if (fechaInicio != null && fechaFin != null) {
            datos = residuoDAO.obtenerReportesPorFecha(fechaInicio, fechaFin);
        } else {
            datos = residuoDAO.obtenerReportePorTipo(); // Todos los registros
        }

        if (datos.isEmpty()) {
            Toast.makeText(this, "No hay datos para exportar", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault());
        String nombreArchivo = "Reporte_" + sdf.format(new Date()) + ".pdf";
        generarPDF(datos, nombreArchivo);
    }

    private void generarPDF(List<ReporteResiduo> datos, String nombreArchivo) {
        PdfDocument documento = new PdfDocument();
        PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page pagina = documento.startPage(paginaInfo);
        Canvas canvas = pagina.getCanvas();

        Paint paint = new Paint();
        paint.setTextSize(12f);
        paint.setColor(Color.BLACK);

        int y = 80;

        canvas.drawText("Reporte de Residuos", 50, y, paint);
        y += 30;

        canvas.drawText("Tipo de Residuo", 50, y, paint);
        canvas.drawText("Cantidad (kg)", 400, y, paint);
        y += 20;

        for (ReporteResiduo item : datos) {
            canvas.drawText(item.tipoResiduo, 50, y, paint);
            canvas.drawText(String.format("%.2f", item.cantidadTotal), 400, y, paint);
            y += 15;
        }

        documento.finishPage(pagina);

        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Ecolim");
        if (!directorio.exists()) directorio.mkdirs();

        File archivo = new File(directorio, nombreArchivo);
        try {
            documento.writeTo(new FileOutputStream(archivo));
            Toast.makeText(this, "PDF guardado en: " + archivo.getPath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        documento.close();
    }
}
