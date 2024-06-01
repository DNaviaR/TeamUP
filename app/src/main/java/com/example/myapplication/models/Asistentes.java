package com.example.myapplication.models;

public class Asistentes {
    private Estadistica estadistica;
    private Liga liga;
    private Xogador xogador;
    private int cantidad;

    public Asistentes(Estadistica estadistica, Liga liga, Xogador xogador, int cantidad) {
        this.estadistica = estadistica;
        this.liga = liga;
        this.xogador = xogador;
        this.cantidad = cantidad;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
