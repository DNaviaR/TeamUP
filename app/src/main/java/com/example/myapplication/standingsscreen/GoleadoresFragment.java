package com.example.myapplication.standingsscreen;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.adapters.GoleadoresAdapter;
import com.example.myapplication.models.Estadistica;
import com.example.myapplication.models.Goleadores;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Xogador;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;


public class GoleadoresFragment extends Fragment {

    private RecyclerView recyclerView;
    private GoleadoresAdapter goleadoresAdapter;
    private ArrayList<Goleadores> goleadoresList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goleadores_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        goleadoresList = new ArrayList<>();
        goleadoresAdapter = new GoleadoresAdapter(goleadoresList);
        recyclerView.setAdapter(goleadoresAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getNombreLiga().observe(getViewLifecycleOwner(), nombreLiga -> {
            if (nombreLiga != null && !nombreLiga.isEmpty()) {
                cargarGoleadores(nombreLiga);
            }
        });

        return view;
    }

    private void cargarGoleadores(String nombreLigaFiltrar) {
        db.collection("estadistica_xogador_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                goleadoresList.clear();
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

                                if ("Goleadores".equals(estadistica.getNombre())) {
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

                                                                Goleadores goleadores = new Goleadores(estadistica, liga, xogador, cantidad);
                                                                goleadoresList.add(goleadores);
                                                                goleadoresList.sort(Comparator.comparingInt(Goleadores::getGoles).reversed());

                                                                goleadoresAdapter.notifyDataSetChanged();
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
                Log.d("GoleadoresFragment", "Error getting documents: ", task.getException());
            }
        });
    }
}
