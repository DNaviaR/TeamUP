package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Liga;

import java.util.ArrayList;

public class LigaAdapter extends RecyclerView.Adapter<LigaAdapter.ItemViewHolder> {

    private ArrayList<Liga> LigaList;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    public LigaAdapter(ArrayList<Liga> LigaList) {
        this.LigaList = LigaList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligas_fila, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Liga currentItem = LigaList.get(position);
        holder.name.setText(currentItem.getNombre());
    }

    @Override
    public int getItemCount() {
        return LigaList.size();
    }
}
