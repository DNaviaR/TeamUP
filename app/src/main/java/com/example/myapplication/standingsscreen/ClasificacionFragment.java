package com.example.myapplication.standingsscreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;


import java.util.ArrayList;

public class ClasificacionFragment extends Fragment {
    private RecyclerView recyclerView;
    private TeamAdapter teamAdapter;
    private ArrayList<Team> teamList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clasificacion, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        teamList = new ArrayList<>();
        teamList.add(new Team("Arsenal", 11, 8, 2, 1, "+23", 26));
        teamList.add(new Team("Man. City", 11, 7, 2, 2, "+16", 23));
        teamList.add(new Team("Liverpool", 11, 7, 2, 2, "+10", 23));
        teamList.add(new Team("Arsenal", 11, 8, 2, 1, "+23", 26));
        teamList.add(new Team("Man. City", 11, 7, 2, 2, "+16", 23));
        teamList.add(new Team("Liverpool", 11, 7, 2, 2, "+10", 23));

        teamAdapter = new TeamAdapter(teamList);
        recyclerView.setAdapter(teamAdapter);

        return view;
    }
}
