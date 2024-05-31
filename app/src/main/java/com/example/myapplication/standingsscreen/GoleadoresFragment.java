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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                                if (estadistica.getNombre().equals("Goleadores")){
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
                                                                Goleadores goleadores= new Goleadores(estadistica,liga,xogador,cantidad);
                                                                goleadoresList.add(goleadores);

                                                                // Ordenar la lista por puntos después de agregar el equipo
                                                                goleadoresList.sort(new Comparator<Goleadores>() {
                                                                    @Override
                                                                    public int compare(Goleadores t1, Goleadores t2) {
                                                                        return Integer.compare(t2.getGoles(), t1.getGoles());
                                                                    }
                                                                });

                                                                // Notificar al adaptador sobre el cambio de datos
                                                                goleadoresAdapter.notifyDataSetChanged();
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