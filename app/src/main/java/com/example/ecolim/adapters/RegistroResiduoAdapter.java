package com.example.ecolim.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;
import com.example.ecolim.models.RegistroResiduo;

import java.util.List;

public class RegistroResiduoAdapter extends RecyclerView.Adapter<RegistroResiduoAdapter.ViewHolder> {

    private List<RegistroResiduo> listaRegistros;

    public RegistroResiduoAdapter(List<RegistroResiduo> listaRegistros) {
        this.listaRegistros = listaRegistros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_registro_r_monitoreo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistroResiduo registro = listaRegistros.get(position);
        holder.txtEmpleadoResiduo.setText(registro.empleado);
        holder.txtTipoResiduo.setText("Tipo: " + registro.tipoResiduo);
        holder.txtCantidadFechaResiduo.setText("Cantidad: " + registro.cantidad + " kg | Fecha: " + registro.fechaRegistro);
        holder.txtObservacionesResiduo.setText(registro.observaciones.isEmpty() ? "Sin observaciones" : registro.observaciones);
    }

    @Override
    public int getItemCount() {
        return listaRegistros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmpleadoResiduo, txtTipoResiduo, txtCantidadFechaResiduo, txtObservacionesResiduo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmpleadoResiduo = itemView.findViewById(R.id.txtEmpleadoResiduo);
            txtTipoResiduo = itemView.findViewById(R.id.txtTipoResiduo);
            txtCantidadFechaResiduo = itemView.findViewById(R.id.txtCantidadFechaResiduo);
            txtObservacionesResiduo = itemView.findViewById(R.id.txtObservacionesResiduo);
        }
    }
}
