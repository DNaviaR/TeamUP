package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private CheckBox checkBoxPrivacy;
    private TextView loginNow;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameEditText = view.findViewById(R.id.editTextNameRegister);
        emailEditText = view.findViewById(R.id.editTextEmailRegister);
        passwordEditText = view.findViewById(R.id.editTextPasswordRegister);
        confirmPasswordEditText = view.findViewById(R.id.editTextConfirmPasswordRegister);
        registerButton = view.findViewById(R.id.ButtonRegister);
        checkBoxPrivacy=view.findViewById(R.id.checkBoxPrivacy);
        loginNow=view.findViewById(R.id.textViewLoginNow);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(v -> registerUser());

        loginNow.setOnClickListener(v -> navigateTo());

        return view;
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), getString(R.string.passwords_distinct), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBoxPrivacy.isChecked()){
            Toast.makeText(getContext(), getString(R.string.should_accept_privacy), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            Map<String, Object> usuario = new HashMap<>();
                                            usuario.put("nombre", name);
                                            usuario.put("email", email);
                                            usuario.put("contraseña", password);

// Add a new document with a generated ID
                                            db.collection("usuarios")
                                                    .add(usuario)
                                                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                                                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                                            Toast.makeText(getContext(), getString(R.string.auth_successful), Toast.LENGTH_SHORT).show();
                                            navigateTo();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.auth_error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Navega al LoginFragment
     */
    private void navigateTo() {
        if (getActivity() instanceof NavigationHost) {
            ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true);
        }
    }
}