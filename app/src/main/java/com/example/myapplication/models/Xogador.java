package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class Xogador {
    private String nombre;
    private String posicion;
    private Long dorsal;
    private Usuario usuario;
    private Equipo equipo;

    public Xogador(String nombre, String posicion, Long dorsal, Usuario usuario, Equipo equipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.dorsal = dorsal;
        this.usuario = usuario;
        this.equipo = equipo;
    }

    public Xogador(Map<String, Object> datos) {
        this.nombre = (String) datos.get("Nombre");
        if (datos.containsKey("Posicion") && datos.get("Posicion") != null) {
            this.posicion = (String) datos.get("Posicion");
        }
        if (datos.containsKey("Dorsal") && datos.get("Dorsal") != null) {
            this.dorsal = (Long) datos.get("Dorsal");
        }
        if (datos.containsKey("Usuario") && datos.get("Usuario") != null) {
            DocumentReference usuarioRef = (DocumentReference) datos.get("Usuario");
            cargarUsuario(usuarioRef);
        }
        if (datos.containsKey("Equipo") && datos.get("Equipo") != null) {
            DocumentReference equipoRef = (DocumentReference) datos.get("Equipo");
            cargarEquipo(equipoRef);
        }
    }

    // Método para cargar el Usuario desde Firestore
    private void cargarUsuario(DocumentReference usuarioRef) {
        usuarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    usuario = new Usuario(document.getData());
                } else {
                    // Manejar el caso en el que el documento no existe
                }
            } else {
                // Manejar errores
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                exception.printStackTrace();
            }
        });
    }

    // Método para cargar el Equipo desde Firestore
    private void cargarEquipo(DocumentReference equipoRef) {
        equipoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    equipo = new Equipo(document.getData());
                } else {
                    // Manejar el caso en el que el documento no existe
                }
            } else {
                // Manejar errores
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                exception.printStackTrace();
            }
        });
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Long getDorsal() {
        return dorsal;
    }

    public void setDorsal(Long dorsal) {
        this.dorsal = dorsal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
