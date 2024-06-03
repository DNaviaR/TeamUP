package com.example.myapplication.matchscreen;

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

import com.example.myapplication.NavigationHost;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.adapters.PartidoAdapter;
import com.example.myapplication.models.EquipoLiga;
import com.example.myapplication.models.Partido;
import com.example.myapplication.standingsscreen.StandingsFragment;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class MatchFragment extends Fragment {
    private RecyclerView recyclerView;
    private PartidoAdapter partidoAdapter;
    private ArrayList<Partido> partidosList;
    private FirebaseFirestore db;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        partidosList = new ArrayList<>();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.match_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        partidoAdapter = new PartidoAdapter(partidosList);
        recyclerView.setAdapter(partidoAdapter);

        // Observa el usuario logueado en el ViewModel
        sharedViewModel.getNombreLiga().observe(getViewLifecycleOwner(), nombreLiga -> {
            if (nombreLiga != null) {
                String nombreLigaFiltrar = nombreLiga;
                cargarPartidos(nombreLigaFiltrar);
            }
        });
        partidoAdapter.setOnItemClickListener(position -> {
            Partido partidoSeleccionado = partidosList.get(position);
            //sharedViewModel.setNombreLiga(partidoSeleccionado.getNombre());
            //Toast.makeText(getContext(), "Seleccionado: " + partidoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();

            // Navegar a otro fragmento si es necesario
            if (getActivity() instanceof NavigationHost) {
                ((NavigationHost) getActivity()).navigateTo(new StandingsFragment(), true);
            }
        });


        return view;
    }

    private void cargarPartidos(String nombreLigaFiltrar) {
        db.collection("partidos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                partidosList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference equipoLocalRef = document.getDocumentReference("Equipo_Local");
                    DocumentReference equipoVisitanteRef = document.getDocumentReference("Equipo_Visitante");
                    Date fecha = document.getDate("Fecha");
                    String resultado = document.getString("Resultado");

                    equipoLocalRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot equipoLocalDoc = task1.getResult();
                            if (equipoLocalDoc.exists()) {
                                EquipoLiga equipoLocal = new EquipoLiga(equipoLocalDoc.getData());

                                equipoVisitanteRef.get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot equipoVisitanteDoc = task2.getResult();
                                        if (equipoVisitanteDoc.exists()) {
                                            EquipoLiga equipoVisitante = new EquipoLiga(equipoVisitanteDoc.getData());

                                            Tasks.whenAll(equipoLocal.getEquipoTask(), equipoLocal.getLigaTask(), equipoVisitante.getEquipoTask(), equipoVisitante.getLigaTask())
                                                    .addOnCompleteListener(task3 -> {
                                                        if (task3.isSuccessful()) {
                                                            // Verificar si ambos equipos son de la misma liga
                                                            if (equipoLocal.getLiga() != null && equipoVisitante.getLiga() != null &&
                                                                    equipoLocal.getLiga().getNombre().equals(nombreLigaFiltrar) &&
                                                                    equipoVisitante.getLiga().getNombre().equals(nombreLigaFiltrar)) {

                                                                Partido partido = new Partido(equipoLocal, equipoVisitante, fecha, resultado);
                                                                partidosList.add(partido);
                                                                partidosList.sort(Comparator.comparing(Partido::getFecha));

                                                                // Notificar al adaptador sobre el cambio de datos
                                                                partidoAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                Log.d("MatchFragment", "Error getting documents: ", task.getException());
            }
        });
    }


}