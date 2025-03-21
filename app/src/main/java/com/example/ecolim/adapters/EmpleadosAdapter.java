package com.example.ecolim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.R;
import com.example.ecolim.models.EmpleadoModel;
import java.util.List;

public class EmpleadosAdapter extends RecyclerView.Adapter<EmpleadosAdapter.EmpleadoViewHolder> {
    private Context context;
    private List<EmpleadoModel> listaEmpleados;
    private OnEmpleadoListener onEmpleadoListener;

    public interface OnEmpleadoListener {
        void onEditClick(EmpleadoModel empleado);
        void onDeleteClick(EmpleadoModel empleado);
        void onToggleActivoClick(EmpleadoModel empleado);
    }

    public EmpleadosAdapter(Context context, List<EmpleadoModel> listaEmpleados) {
        this.context = context;
        this.listaEmpleados = listaEmpleados;
    }

    public void setOnEmpleadoListener(OnEmpleadoListener listener) {
        this.onEmpleadoListener = listener;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_empleado, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        EmpleadoModel empleado = listaEmpleados.get(position);
        holder.tvNombre.setText(empleado.getNombre());
        holder.tvEmail.setText(empleado.getEmail());
        holder.tvCargo.setText(empleado.getCargo());
        holder.tvDepartamento.setText(empleado.getDepartamento());
        
        // Cambiar el ícono según el estado activo/inactivo
        holder.btnToggleActivo.setImageResource(
            empleado.isActivo() ? R.drawable.inicio_punto_activo : R.drawable.inicio_punto_inactivo
        );

        holder.btnEditar.setOnClickListener(v -> {
            if (onEmpleadoListener != null) {
                onEmpleadoListener.onEditClick(empleado);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (onEmpleadoListener != null) {
                onEmpleadoListener.onDeleteClick(empleado);
            }
        });

        holder.btnToggleActivo.setOnClickListener(v -> {
            if (onEmpleadoListener != null) {
                onEmpleadoListener.onToggleActivoClick(empleado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public void actualizarLista(List<EmpleadoModel> nuevaLista) {
        this.listaEmpleados = nuevaLista;
        notifyDataSetChanged();
    }

    static class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEmail, tvCargo, tvDepartamento;
        ImageButton btnEditar, btnEliminar, btnToggleActivo;

        EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvCargo = itemView.findViewById(R.id.tvCargo);
            tvDepartamento = itemView.findViewById(R.id.tvDepartamento);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnToggleActivo = itemView.findViewById(R.id.btnToggleActivo);
        }
    }
}
