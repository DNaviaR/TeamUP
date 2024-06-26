package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.EquipoLiga;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private ArrayList<EquipoLiga> equipoLigaList;

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView position, name, played, wins, draws, losses, goalDifference, points;

        public ItemViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.equipoLiga);
            played = itemView.findViewById(R.id.played);
            wins = itemView.findViewById(R.id.wins);
            draws = itemView.findViewById(R.id.draws);
            losses = itemView.findViewById(R.id.losses);
            goalDifference = itemView.findViewById(R.id.goal_difference);
            points = itemView.findViewById(R.id.points);
        }
    }

    public TeamAdapter(ArrayList<EquipoLiga> equipoLigaList) {
        this.equipoLigaList = equipoLigaList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clasificacion_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clasificacion_fila, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            EquipoLiga currentItem = equipoLigaList.get(position - 1); // Ajuste de posición por la cabecera
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.position.setText(String.valueOf(position)); // Usando la posición del RecyclerView ajustada
            itemViewHolder.name.setText(currentItem.getEquipo().getNombre());
            itemViewHolder.played.setText(String.valueOf(currentItem.getPartidostotales()));
            itemViewHolder.wins.setText(String.valueOf(currentItem.getPartidosGanados()));
            itemViewHolder.draws.setText(String.valueOf(currentItem.getPartidosEmpatados()));
            itemViewHolder.losses.setText(String.valueOf(currentItem.getPartidosPerdidos()));
            itemViewHolder.goalDifference.setText(String.valueOf(currentItem.getDiferenciaGoles()));
            itemViewHolder.points.setText(String.valueOf(currentItem.getPuntos()));
        }
    }

    @Override
    public int getItemCount() {
        return equipoLigaList.size() + 1; // +1 por la cabecera
    }
}
