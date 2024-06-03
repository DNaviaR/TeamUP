package com.example.myapplication.models;

import java.util.Map;

public class Equipo {
    private String nombre;

    public Equipo() {
    }

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    public Equipo(Map<String, Object> datos) {
        this.nombre = (String) datos.get("Nombre");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
