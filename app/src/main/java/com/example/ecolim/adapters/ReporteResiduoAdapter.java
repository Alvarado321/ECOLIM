package com.example.ecolim.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.R;
import com.example.ecolim.models.ReporteResiduo;
import java.util.List;

public class ReporteResiduoAdapter extends RecyclerView.Adapter<ReporteResiduoAdapter.ViewHolder> {

    private List<ReporteResiduo> listaReportes;

    public ReporteResiduoAdapter(List<ReporteResiduo> listaReportes) {
        this.listaReportes = listaReportes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_registro_r_reportes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReporteResiduo reporte = listaReportes.get(position);
        holder.txtTipoResiduo.setText(reporte.tipoResiduo);
        holder.txtCantidadResiduo.setText(String.format("%.2f kg", reporte.cantidadTotal));
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public void actualizarDatos(List<ReporteResiduo> nuevaLista) {
        this.listaReportes.clear();
        this.listaReportes.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTipoResiduo, txtCantidadResiduo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTipoResiduo = itemView.findViewById(R.id.txtTipoResiduoReporte);
            txtCantidadResiduo = itemView.findViewById(R.id.txtCantidadResiduoReporte);
        }
    }
}
