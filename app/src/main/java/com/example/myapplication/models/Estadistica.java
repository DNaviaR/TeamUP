package com.example.myapplication.models;

import java.util.Map;

public class Estadistica {
    private String nombre;

    public Estadistica(String nombre) {
        this.nombre = nombre;
    }

    public Estadistica(Map<String, Object> datos) {
        this.nombre = (String) datos.get("Nombre");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
