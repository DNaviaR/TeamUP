package com.example.myapplication.profilescreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Usuario;
import com.example.myapplication.models.UsuarioLiga;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class ProfileFragment extends Fragment {
    private FirebaseFirestore db;
    private SharedViewModel sharedViewModel;
    private TextView user_name;
    private TextView user_role;
    private TextView logoutButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        user_name = view.findViewById(R.id.user_name);
        user_role = view.findViewById(R.id.user_role);
        logoutButton = view.findViewById(R.id.logout_button);

        // Observa el usuario logueado en el ViewModel
        try {
            sharedViewModel.getLigaInfo().observe(getViewLifecycleOwner(), ligaInfo -> {
                if (ligaInfo != null) {
                    Usuario usuario = ligaInfo.getUsuario();
                    String nombreLigaFiltrar = ligaInfo.getNombreLiga();
                    cargarPerfil(usuario, nombreLigaFiltrar);
                }
            });

        }catch (Exception e) {
            user_name.setText(getString(R.string.unknown));
            user_role.setText(getString(R.string.unknown));
        }

        logoutButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logoutAndCloseApp();
            }
        });

        return view;
    }
    private void cargarPerfil(Usuario usuarioFiltrar, String nombreLigaFiltrar) {
        db.collection("usuario_liga").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference ligaRef = document.getDocumentReference("Liga_ID");
                    DocumentReference usuarioRef = document.getDocumentReference("Usuario_ID");
                    String rol=document.getString("Rol");

                    ligaRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot ligaDoc = task1.getResult();
                            if (ligaDoc.exists()) {
                                Liga liga = new Liga(ligaDoc.getData());
                                if (nombreLigaFiltrar.equals(liga.getNombre())) {
                                    usuarioRef.get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            DocumentSnapshot usuarioDoc = task2.getResult();
                                            if (usuarioDoc.exists()) {
                                                Usuario usuario = new Usuario(usuarioDoc.getData());
                                                if (usuario.equals(usuarioFiltrar)){
                                                    UsuarioLiga usuarioLiga=new UsuarioLiga(liga,usuario,rol);
                                                    user_name.setText(usuarioLiga.getUsuario().getNombre());
                                                    user_role.setText(usuarioLiga.getRol());
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
                Log.d("MatchFragment", "Error getting documents: ", task.getException());
                user_name.setText(getString(R.string.unknown));
                user_role.setText(getString(R.string.unknown));
            }
        });
    }
}