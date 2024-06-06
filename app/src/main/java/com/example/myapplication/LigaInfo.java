package com.example.myapplication;

import com.example.myapplication.models.Usuario;

public class LigaInfo {
    private Usuario usuario;
    private String nombreLiga;

    public LigaInfo(Usuario usuario, String nombreLiga) {
        this.usuario = usuario;
        this.nombreLiga = nombreLiga;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }
}
