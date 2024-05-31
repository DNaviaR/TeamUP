package com.example.myapplication.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class Usuario {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private String nombre;
    private String email;


    public Usuario() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user!=null){
            this.nombre = user.getDisplayName();
            this.email = user.getEmail();
        }
    }

    public Usuario(Map<String, Object> datos) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        this.nombre = (String) datos.get("nombre");
        this.email = (String) datos.get("email");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
