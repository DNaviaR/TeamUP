package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.homescreen.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils;


public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerNowButton;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.editTextEmailLogin);
        passwordEditText = view.findViewById(R.id.editTextPasswordLogin);
        loginButton = view.findViewById(R.id.ButtonLoginSignIn);
        registerNowButton = view.findViewById(R.id.textViewLoginRegisterNow);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> loginUser());

        registerNowButton.setOnClickListener(v -> navigateToRegister());

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), getString(R.string.auth_successful), Toast.LENGTH_SHORT).show();
                        // Update UI with the signed-in user's information
                        updateUI(user);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.auth_error), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (getActivity() instanceof NavigationHost) {
                ((NavigationHost) getActivity()).navigateTo(new HomeFragment(), true);
            }
        } else {
            // User is signed out
            // Stay on the login screen
        }
    }

    /**
     * Navega al RegisterFragment
     */
    private void navigateToRegister() {
        if (getActivity() instanceof NavigationHost) {
            ((NavigationHost) getActivity()).navigateTo(new RegisterFragment(), true);
        }
    }
}