package com.example.myapplication.models;

import java.util.Map;

public class Xogador {
    private String nombre;
    private String posicion;
    private int dorsal;
    private Usuario usuario;

    public Xogador(String nombre, String posicion, int dorsal, Usuario usuario) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.dorsal = dorsal;
        this.usuario = usuario;
    }

    public Xogador(Map<String, Object> datos) {
        this.nombre = (String) datos.get("Nombre");
        if (datos.containsKey("Posicion") && datos.get("Posicion") != null){
            this.posicion = posicion;
        }
        if (datos.containsKey("Dorsal") && datos.get("Dorsal") != null){
            this.dorsal = dorsal;
        }
        if (datos.containsKey("Usuario") && datos.get("Usuario") != null){
            this.usuario = usuario;
        }
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

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
