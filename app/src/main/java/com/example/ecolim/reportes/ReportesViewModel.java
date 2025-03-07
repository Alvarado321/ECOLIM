package com.example.ecolim.reportes;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import com.example.ecolim.helpers.ResiduoDAO;
import com.example.ecolim.models.ReporteResiduo;
import java.util.List;

public class ReportesViewModel extends ViewModel {

    private ResiduoDAO residuoDAO;

    public ReportesViewModel(Context context) {
        residuoDAO = new ResiduoDAO(context);
    }

    public List<ReporteResiduo> getReportePorTipo() {
        return residuoDAO.obtenerReportePorTipo();
    }
}
