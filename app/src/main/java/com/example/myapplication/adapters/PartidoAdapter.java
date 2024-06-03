package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnItemClickListener;
import com.example.myapplication.R;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Partido;

import java.util.ArrayList;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.ItemViewHolder> {

    private ArrayList<Partido> partidoList;
    private OnItemClickListener listener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView equipo_local,equipo_visitante,fecha;

        public ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            equipo_local = itemView.findViewById(R.id.equipo_local);
            equipo_visitante = itemView.findViewById(R.id.equipo_visitante);
            fecha = itemView.findViewById(R.id.fecha);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public PartidoAdapter(ArrayList<Partido> ligaList) {
        this.partidoList = ligaList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_fila, parent, false);
        return new ItemViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Partido currentItem = partidoList.get(position);

        if (currentItem.getEquipoLocal() != null && currentItem.getEquipoLocal().getEquipo() != null) {
            holder.equipo_local.setText(currentItem.getEquipoLocal().getEquipo().getNombre());
        } else {
            holder.equipo_local.setText("Desconocido");
        }

        if (currentItem.getEquipoVisitante() != null && currentItem.getEquipoVisitante().getEquipo() != null) {
            holder.equipo_visitante.setText(currentItem.getEquipoVisitante().getEquipo().getNombre());
        } else {
            holder.equipo_visitante.setText("Desconocido");
        }

        if (currentItem.getFecha() != null) {
            holder.fecha.setText(currentItem.getFecha().toString());
        } else {
            holder.fecha.setText("Fecha no disponible");
        }
    }


    @Override
    public int getItemCount() {
        return partidoList.size();
    }
}


