package com.example.ecolim.reportes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.R;
import com.example.ecolim.adapters.ReporteResiduoAdapter;

public class ReportesFragment extends Fragment {

    private RecyclerView recyclerReportes;
    private ReportesViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_registro_r_reportes, container, false);

        recyclerReportes = root.findViewById(R.id.recyclerReportes);
        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ReportesViewModel(getContext());
        cargarReporte();

        return root;
    }

    void cargarReporte() {
        ReporteResiduoAdapter adapter = new ReporteResiduoAdapter(viewModel.getReportePorTipo());
        recyclerReportes.setAdapter(adapter);
    }
}
