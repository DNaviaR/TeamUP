package com.example.myapplication.homescreen;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.LigaAdapter;
import com.example.myapplication.models.Equipo_Liga;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Usuario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LigaAdapter ligaAdapter;
    private ArrayList<Liga> LigaList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        LigaList = new ArrayList<>();
        ligaAdapter = new LigaAdapter(LigaList);
        recyclerView.setAdapter(ligaAdapter);

        // Define el nombre de la liga que deseas filtrar
        String emailUsuarioFiltrar = "a@a.com";

        // Paso 1: Consultar usuario_liga
        db.collection("usuario_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference usuarioRef = document.getDocumentReference("Usuario_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");

                    // Paso 2: Consultar los nombres de usuario y liga
                    usuarioRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot usuarioDoc = task1.getResult();
                            if (usuarioDoc.exists()) {
                                Usuario usuario = new Usuario(usuarioDoc.getData());
                                if (usuario.getUser().getEmail().equals(emailUsuarioFiltrar)){
                                    ligaRef.get().addOnCompleteListener(task11 -> {
                                        if (task11.isSuccessful()) {
                                            DocumentSnapshot ligaDoc = task11.getResult();
                                            if (ligaDoc.exists()) {
                                                Liga liga = new Liga(ligaDoc.getData());
                                                LigaList.add(liga);
                                                LigaList.sort(new Comparator<Liga>() {
                                                    @Override
                                                    public int compare(Liga t1, Liga t2) {
                                                        return t1.getNombre().compareTo(t2.getNombre());
                                                    }
                                                });

                                                // Notificar al adaptador sobre el cambio de datos
                                                ligaAdapter.notifyDataSetChanged();

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
