package com.example.myapplication.models;

public class UsuarioLiga {
    private Liga liga;
    private Usuario usuario;
    private String rol;

    public UsuarioLiga(Liga liga, Usuario usuario, String rol) {
        this.liga = liga;
        this.usuario = usuario;
        this.rol=rol;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
