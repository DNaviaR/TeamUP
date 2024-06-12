package com.example.myapplication.homescreen;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NavigationHost;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.adapters.LigaAdapter;
import com.example.myapplication.models.Liga;
import com.example.myapplication.models.Usuario;
import com.example.myapplication.standingsscreen.StandingsFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LigaAdapter ligaAdapter;
    private ArrayList<Liga> ligaList;
    private FirebaseFirestore db;
    private SharedViewModel sharedViewModel;
    private Button ButtonCrearLiga;
    private Button ButtonUnirseLiga;

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
        ButtonCrearLiga=view.findViewById(R.id.ButtonCrearLiga);
        ButtonUnirseLiga=view.findViewById(R.id.ButtonUnirseLiga);

        ButtonCrearLiga.setOnClickListener(v -> showLeagueDialog(getString(R.string.createleague)));
        ButtonUnirseLiga.setOnClickListener(v -> showLeagueDialog(getString(R.string.joinleague)));

        // Observa el usuario logueado en el ViewModel
        sharedViewModel.getUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                String emailUsuarioFiltrar = usuario.getEmail();
                cargarLigas(emailUsuarioFiltrar);
            }
        });
        ligaAdapter.setOnItemClickListener(position -> {
            Liga ligaSeleccionada = ligaList.get(position);
            sharedViewModel.setNombreLiga(ligaSeleccionada.getNombre());
            Toast.makeText(getContext(), "Seleccionado: " + ligaSeleccionada.getNombre(), Toast.LENGTH_SHORT).show();

            // Navegar a otro fragmento si es necesario
            if (getActivity() instanceof NavigationHost) {
                ((NavigationHost) getActivity()).navigateTo(new StandingsFragment(), true);
            }

            // Cambiar la selección de BottomNavigationView
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setBottomNavigationItem(R.id.standings);
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

    private void showLeagueDialog(String title) {
        boolean isEdit;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);
        String joinLeagueString = getString(R.string.joinleague);

        isEdit = title.equals(joinLeagueString);

        View dialogView = getLayoutInflater().inflate(R.layout.crear_unirse_liga, null);
        builder.setView(dialogView);

        EditText leagueNameEditText = dialogView.findViewById(R.id.league_name);
        EditText leagueCodeEditText = dialogView.findViewById(R.id.league_code);

        builder.setPositiveButton(getString(R.string.accept), (dialog, which) -> {
            String leagueName = leagueNameEditText.getText().toString().trim();
            String leagueCode = leagueCodeEditText.getText().toString().trim();
            if (!leagueName.isEmpty() && !leagueCode.isEmpty()) {
                if (!isEdit){
                    Map<String, Object> liga = new HashMap<>();
                    liga.put("nombre", leagueName);
                    liga.put("codigo", leagueCode);
                    db.collection("ligas")
                            .add(liga)
                            .addOnSuccessListener(documentReference -> {
                                String rol="Admin";
                                // Usa el ID del documento en otra operación
                                asociarLigaUsuario(documentReference, rol);
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                }else{
                    String rol="Usuario";
                    db.collection("ligas")
                            .whereEqualTo("codigo", leagueCode)
                            .whereEqualTo("nombre", leagueName)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documents : queryDocumentSnapshots) {
                                    DocumentReference ligaRef = documents.getReference();
                                    asociarLigaUsuario(ligaRef,rol);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error al realizar la consulta", e);
                                Toast.makeText(getContext(), "No hay ninguna liga con ese nombre o codigo.", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        builder.setNegativeButton(getString(R.string.camcel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void asociarLigaUsuario(DocumentReference document, String rol) {
        Usuario usuario=new Usuario();
        db.collection("usuarios")
                .whereEqualTo("email", usuario.getEmail())
                .whereEqualTo("nombre", usuario.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documents : queryDocumentSnapshots) {

                        DocumentReference usuarioRef = documents.getReference();
                        Map<String, Object> usuarioLiga = new HashMap<>();
                        usuarioLiga.put("Liga_ID", document);
                        usuarioLiga.put("Rol", rol);
                        usuarioLiga.put("Usuario_ID", usuarioRef);

                        db.collection("usuario_liga")
                                .add(usuarioLiga)
                                .addOnSuccessListener(docRef -> Log.d(TAG, "Another document added with ID: " + docRef.getId()))
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding another document", e));
                        cargarLigas(usuario.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error al realizar la consulta", e);
                });
    }

}

