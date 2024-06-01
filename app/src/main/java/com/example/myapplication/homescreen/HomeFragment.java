package com.example.myapplication.homescreen;

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
import com.example.myapplication.adapters.LigaAdapter;
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
    private ArrayList<Liga> ligaList;
    private FirebaseFirestore db;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        ligaList = new ArrayList<>();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ligaAdapter = new LigaAdapter(ligaList);
        recyclerView.setAdapter(ligaAdapter);

        // Observa el usuario logueado en el ViewModel
        sharedViewModel.getUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                String emailUsuarioFiltrar = usuario.getEmail();
                cargarLigas(emailUsuarioFiltrar);
            }
        });

        return view;
    }

    private void cargarLigas(String emailUsuarioFiltrar) {
        db.collection("usuario_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ligaList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference usuarioRef = document.getDocumentReference("Usuario_ID");
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");

                    // Consultar los nombres de usuario y liga
                    usuarioRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot usuarioDoc = task1.getResult();
                            if (usuarioDoc.exists()) {
                                Usuario usuarioTabla = new Usuario(usuarioDoc.getData());
                                if (usuarioTabla.getEmail().equals(emailUsuarioFiltrar)) {
                                    ligaRef.get().addOnCompleteListener(task11 -> {
                                        if (task11.isSuccessful()) {
                                            DocumentSnapshot ligaDoc = task11.getResult();
                                            if (ligaDoc.exists()) {
                                                Liga liga = new Liga(ligaDoc.getData());
                                                ligaList.add(liga);
                                                ligaList.sort(Comparator.comparing(Liga::getNombre));

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
                Log.d("HomeFragment", "Error getting documents: ", task.getException());
            }
        });
    }
}

