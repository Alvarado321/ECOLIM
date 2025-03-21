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
import com.example.ecolim.models.UsuarioModel;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {
    private Context context;
    private List<UsuarioModel> listaUsuarios;
    private OnUsuarioListener onUsuarioListener;

    public interface OnUsuarioListener {
        void onEditClick(UsuarioModel usuario);
        void onDeleteClick(UsuarioModel usuario);
    }

    public UsuariosAdapter(Context context, List<UsuarioModel> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    public void setOnUsuarioListener(OnUsuarioListener listener) {
        this.onUsuarioListener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioModel usuario = listaUsuarios.get(position);
        holder.tvNombre.setText(usuario.getNombre());
        holder.tvEmail.setText(usuario.getEmail());
        holder.tvRol.setText(usuario.getRol());

        holder.btnEditar.setOnClickListener(v -> {
            if (onUsuarioListener != null) {
                onUsuarioListener.onEditClick(usuario);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (onUsuarioListener != null) {
                onUsuarioListener.onDeleteClick(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void actualizarLista(List<UsuarioModel> nuevaLista) {
        this.listaUsuarios = nuevaLista;
        notifyDataSetChanged();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEmail, tvRol;
        ImageButton btnEditar, btnEliminar;

        UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRol = itemView.findViewById(R.id.tvRol);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
