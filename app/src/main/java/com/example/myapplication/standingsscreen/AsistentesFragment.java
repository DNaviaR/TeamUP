package com.example.myapplication.standingsscreen;

import android.os.Bundle;

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
import com.example.myapplication.adapters.AsistentesAdapter;
import com.example.myapplication.models.Asistentes;
import com.example.myapplication.models.Estadistica;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Xogador;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class AsistentesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AsistentesAdapter asistentesAdapter;
    private ArrayList<Asistentes> asistentesList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asistentes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        asistentesList = new ArrayList<>();
        asistentesAdapter = new AsistentesAdapter(asistentesList);
        recyclerView.setAdapter(asistentesAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getNombreLiga().observe(getViewLifecycleOwner(), nombreLiga -> {
            if (nombreLiga != null && !nombreLiga.isEmpty()) {
                cargarAsistentes(nombreLiga);
            }
        });

        return view;
    }

    private void cargarAsistentes(String nombreLigaFiltrar) {
        db.collection("estadistica_xogador_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                asistentesList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference estadisticaRef = document.getDocumentReference("Estadistica_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");
                    DocumentReference xogadorRef = document.getDocumentReference("Xogador_ID");
                    int cantidad = document.getLong("Cantidad").intValue();

                    estadisticaRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot estadisticaDoc = task1.getResult();
                            if (estadisticaDoc.exists()) {
                                Estadistica estadistica = new Estadistica(estadisticaDoc.getData());

                                if ("Asistentes".equals(estadistica.getNombre())) {
                                    ligaRef.get().addOnCompleteListener(task11 -> {
                                        if (task11.isSuccessful()) {
                                            DocumentSnapshot ligaDoc = task11.getResult();
                                            if (ligaDoc.exists()) {
                                                Liga liga = new Liga(ligaDoc.getData());

                                                if (nombreLigaFiltrar.equals(liga.getNombre())) {
                                                    xogadorRef.get().addOnCompleteListener(task111 -> {
                                                        if (task111.isSuccessful()) {
                                                            DocumentSnapshot xogadorDoc = task111.getResult();
                                                            if (xogadorDoc.exists()) {
                                                                Xogador xogador = new Xogador(xogadorDoc.getData());

                                                                Asistentes asistentes = new Asistentes(estadistica, liga, xogador, cantidad);
                                                                asistentesList.add(asistentes);
                                                                asistentesList.sort(Comparator.comparingInt(Asistentes::getCantidad).reversed());

                                                                asistentesAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            } else {
                Log.d("AsistentesFragment", "Error getting documents: ", task.getException());
            }
        });
    }
}
