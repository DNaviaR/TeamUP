package com.example.myapplication.models;

public class UsuarioLiga {
    private Liga liga;
    private Usuario usuario;

    public UsuarioLiga(Liga liga, Usuario usuario) {
        this.liga = liga;
        this.usuario = usuario;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
