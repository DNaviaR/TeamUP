package com.example.myapplication.standingsscreen;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_asistentes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        asistentesList = new ArrayList<>();
        asistentesAdapter = new AsistentesAdapter(asistentesList);
        recyclerView.setAdapter(asistentesAdapter);

        // Define el nombre de la liga que deseas filtrar
        String nombreLigaFiltrar = "Liga Pruebas";

        // Paso 1: Consultar equipo_liga
        db.collection("estadistica_xogador_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference estadisticaRef = document.getDocumentReference("Estadistica_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");
                    DocumentReference xogadorRef = document.getDocumentReference("Xogador_ID");
                    int cantidad = document.getLong("Cantidad").intValue();

                    // Paso 2: Consultar los nombres de equipo y liga
                    estadisticaRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot estadisticaDoc = task1.getResult();
                            if (estadisticaDoc.exists()) {
                                Estadistica estadistica = new Estadistica(estadisticaDoc.getData());

                                if (estadistica.getNombre().equals("Asistentes")){
                                    ligaRef.get().addOnCompleteListener(task11 -> {
                                        if (task11.isSuccessful()) {
                                            DocumentSnapshot ligaDoc = task11.getResult();
                                            if (ligaDoc.exists()) {
                                                Liga liga = new Liga(ligaDoc.getData());

                                                xogadorRef.get().addOnCompleteListener(task111 -> {
                                                    if (task111.isSuccessful()) {
                                                        DocumentSnapshot xogadorDoc = task111.getResult();
                                                        if (xogadorDoc.exists()) {
                                                            Xogador xogador = new Xogador(xogadorDoc.getData());

                                                            // Filtrar por el nombre de la liga
                                                            if (liga.getNombre() != null && liga.getNombre().equals(nombreLigaFiltrar)) {
                                                                // Crear un objeto Team con todos los datos combinados
                                                                Asistentes asistentes= new Asistentes(estadistica,liga,xogador,cantidad);
                                                                asistentesList.add(asistentes);

                                                                // Ordenar la lista por puntos después de agregar el equipo
                                                                asistentesList.sort(new Comparator<Asistentes>() {
                                                                    @Override
                                                                    public int compare(Asistentes t1, Asistentes t2) {
                                                                        return Integer.compare(t2.getCantidad(), t1.getCantidad());
                                                                    }
                                                                });

                                                                // Notificar al adaptador sobre el cambio de datos
                                                                asistentesAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
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