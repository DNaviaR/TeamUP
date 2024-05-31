package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Goleadores;

import java.util.ArrayList;

public class GoleadoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private ArrayList<Goleadores> goleadoresList;

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView position, player, goals;

        public ItemViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            player = itemView.findViewById(R.id.player);
            goals = itemView.findViewById(R.id.goals);
        }
    }

    public GoleadoresAdapter(ArrayList<Goleadores> goleadoresList) {
        this.goleadoresList = goleadoresList;
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goleadores_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goleadores_fila, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Goleadores currentItem = goleadoresList.get(position - 1); // Ajuste de posición por la cabecera
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.position.setText(String.valueOf(position)); // Usando la posición del RecyclerView ajustada
            itemViewHolder.player.setText(currentItem.getXogador().getNombre());
            itemViewHolder.goals.setText(String.valueOf(currentItem.getGoles()));
        }
    }

    @Override
    public int getItemCount() {
        return goleadoresList.size() + 1; // +1 por la cabecera
    }
}
