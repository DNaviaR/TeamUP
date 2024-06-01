package com.example.myapplication.standingsscreen;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.adapters.TeamAdapter;
import com.example.myapplication.models.Equipo;
import com.example.myapplication.models.Equipo_Liga;
import com.example.myapplication.models.Liga;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Comparator;

public class ClasificacionFragment extends Fragment {
    private RecyclerView recyclerView;
    private TeamAdapter teamAdapter;
    private ArrayList<Equipo_Liga> equipoLigaList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clasificacion_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        equipoLigaList = new ArrayList<>();
        teamAdapter = new TeamAdapter(equipoLigaList);
        recyclerView.setAdapter(teamAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getNombreLiga().observe(getViewLifecycleOwner(), nombreLiga -> {
            if (nombreLiga != null && !nombreLiga.isEmpty()) {
                cargarClasificacion(nombreLiga);
            }
        });
        return view;
    }

    private void cargarClasificacion(String nombreLigaFiltrar) {
        db.collection("equipo_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                equipoLigaList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference equipoRef = document.getDocumentReference("Equipo_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");
                    int diferenciaGoles = document.getLong("Diferencia_Goles").intValue();
                    int partidosEmpatados = document.getLong("Partidos_Empatados").intValue();
                    int partidosGanados = document.getLong("Partidos_Ganados").intValue();
                    int partidosPerdidos = document.getLong("Partidos_Perdidos").intValue();
                    int puntos = document.getLong("Puntos").intValue();

                    equipoRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot equipoDoc = task1.getResult();
                            if (equipoDoc.exists()) {
                                Equipo equipo = new Equipo(equipoDoc.getData());

                                ligaRef.get().addOnCompleteListener(task11 -> {
                                    if (task11.isSuccessful()) {
                                        DocumentSnapshot ligaDoc = task11.getResult();
                                        if (ligaDoc.exists()) {
                                            Liga liga = new Liga(ligaDoc.getData());

                                            if (liga.getNombre() != null && liga.getNombre().equals(nombreLigaFiltrar)) {
                                                Equipo_Liga equipoLiga = new Equipo_Liga(equipo, liga, partidosGanados, partidosEmpatados, partidosPerdidos, diferenciaGoles, puntos);
                                                equipoLigaList.add(equipoLiga);
                                                equipoLigaList.sort((t1, t2) -> Integer.compare(t2.getPuntos(), t1.getPuntos()));
                                                teamAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                Log.d("ClasificacionFragment", "Error getting documents: ", task.getException());
            }
        });
    }
}
