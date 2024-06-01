package com.example.myapplication.models;

public class Equipo_Liga {
    private Equipo equipo;
    private Liga liga;
    private int partidostotales;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int diferenciaGoles;
    private int puntos;

    public Equipo_Liga(Equipo equipo, Liga liga, int partidosGanados, int partidosEmpatados, int partidosPerdidos, int diferenciaGoles, int puntos) {
        this.equipo = equipo;
        this.liga = liga;
        this.partidosGanados = partidosGanados;
        this.partidosEmpatados = partidosEmpatados;
        this.partidosPerdidos = partidosPerdidos;
        this.partidostotales = partidosGanados+partidosEmpatados+partidosPerdidos;
        this.diferenciaGoles = diferenciaGoles;
        this.puntos = puntos;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public int getPartidostotales() {
        return partidostotales;
    }

    public void setPartidostotales(int partidostotales) {
        this.partidostotales = partidostotales;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public void setPartidosEmpatados(int partidosEmpatados) {
        this.partidosEmpatados = partidosEmpatados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public void setPartidosPerdidos(int partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    public int getDiferenciaGoles() {
        return diferenciaGoles;
    }

    public void setDiferenciaGoles(int diferenciaGoles) {
        this.diferenciaGoles = diferenciaGoles;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}