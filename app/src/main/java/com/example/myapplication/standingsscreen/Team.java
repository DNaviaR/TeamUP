package com.example.myapplication.standingsscreen;

import com.google.firebase.firestore.DocumentReference;

public class Team {
    private DocumentReference equipoRef;
    private DocumentReference ligaRef;
    private String nombreEquipo;
    private String nombreLiga;
    private int partidostotales;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int diferenciaGoles;
    private int puntos;

    // Constructor
    public Team(DocumentReference equipoRef, DocumentReference ligaRef, String nombreEquipo, String nombreLiga, int partidosGanados, int partidosEmpatados, int partidosPerdidos, int diferenciaGoles, int puntos) {
        this.equipoRef = equipoRef;
        this.ligaRef = ligaRef;
        this.nombreEquipo=nombreEquipo;
        this.nombreLiga=nombreLiga;
        this.partidosGanados = partidosGanados;
        this.partidosEmpatados = partidosEmpatados;
        this.partidosPerdidos = partidosPerdidos;
        this.partidostotales=partidosGanados+partidosEmpatados+partidosPerdidos;
        this.diferenciaGoles = diferenciaGoles;
        this.puntos = puntos;
    }

    // Getters y setters

    public DocumentReference getEquipoRef() {
        return equipoRef;
    }

    public void setEquipoRef(DocumentReference equipoRef) {
        this.equipoRef = equipoRef;
    }

    public DocumentReference getLigaRef() {
        return ligaRef;
    }

    public void setLigaRef(DocumentReference ligaRef) {
        this.ligaRef = ligaRef;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }

    public int getPartidosTotales() {
        return partidostotales;
    }

    public void setPartidosTotales(int partidostotales) {
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