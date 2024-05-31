package com.example.myapplication.models;

import java.util.Map;

public class Liga {
    private String nombre;
    private String codigo;

    public Liga(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Liga(Map<String, Object> datos) {
        this.nombre = (String) datos.get("nombre");
        this.codigo = (String) datos.get("codigo");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
