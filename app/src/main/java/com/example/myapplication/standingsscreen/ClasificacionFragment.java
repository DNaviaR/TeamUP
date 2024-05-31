package com.example.myapplication.standingsscreen;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.R;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        // Define el nombre de la liga que deseas filtrar
        String nombreLigaFiltrar = "Liga Pruebas";

        // Paso 1: Consultar equipo_liga
        db.collection("equipo_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference equipoRef = document.getDocumentReference("Equipo_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");
                    int diferenciaGoles = document.getLong("Diferencia_Goles").intValue();
                    int partidosEmpatados = document.getLong("Partidos_Empatados").intValue();
                    int partidosGanados = document.getLong("Partidos_Ganados").intValue();
                    int partidosPerdidos = document.getLong("Partidos_Perdidos").intValue();
                    int puntos = document.getLong("Puntos").intValue();

                    // Paso 2: Consultar los nombres de equipo y liga
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

                                            // Filtrar por el nombre de la liga
                                            if (liga.getNombre() != null && liga.getNombre().equals(nombreLigaFiltrar)) {
                                                // Crear un objeto Team con todos los datos combinados
                                                Equipo_Liga equipoLiga = new Equipo_Liga(equipo, liga, partidosGanados, partidosEmpatados, partidosPerdidos, diferenciaGoles, puntos);
                                                equipoLigaList.add(equipoLiga);

                                                // Ordenar la lista por puntos después de agregar el equipo
                                                equipoLigaList.sort(new Comparator<Equipo_Liga>() {
                                                    @Override
                                                    public int compare(Equipo_Liga t1, Equipo_Liga t2) {
                                                        return Integer.compare(t2.getPuntos(), t1.getPuntos());
                                                    }
                                                });

                                                // Notificar al adaptador sobre el cambio de datos
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
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


        return view;
    }
}
