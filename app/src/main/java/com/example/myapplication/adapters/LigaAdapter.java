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

import java.util.ArrayList;

public class LigaAdapter extends RecyclerView.Adapter<LigaAdapter.ItemViewHolder> {

    private ArrayList<Liga> ligaList;
    private OnItemClickListener listener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
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

    public LigaAdapter(ArrayList<Liga> ligaList) {
        this.ligaList = ligaList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligas_fila, parent, false);
        return new ItemViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Liga currentItem = ligaList.get(position);
        holder.name.setText(currentItem.getNombre());
    }

    @Override
    public int getItemCount() {
        return ligaList.size();
    }
}

