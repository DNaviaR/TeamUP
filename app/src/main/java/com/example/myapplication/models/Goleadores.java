package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentReference;

public class Goleadores {
    private Estadistica estadistica;
    private Liga liga;
    private Xogador xogador;
    private int goles;

    public Goleadores(Estadistica estadistica, Liga liga, Xogador xogador, int goles) {
        this.estadistica = estadistica;
        this.liga = liga;
        this.xogador = xogador;
        this.goles = goles;
    }

    public Estadistica getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(Estadistica estadistica) {
        this.estadistica = estadistica;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public Xogador getXogador() {
        return xogador;
    }

    public void setXogador(Xogador xogador) {
        this.xogador = xogador;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }
}
