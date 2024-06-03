package com.example.myapplication.models;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.sql.Date;
import java.util.Map;

public class EquipoLiga {
    private Equipo equipo;
    private Liga liga;
    private Long partidostotales;
    private Long partidosGanados;
    private Long partidosEmpatados;
    private Long partidosPerdidos;
    private Long diferenciaGoles;
    private Long puntos;

    private TaskCompletionSource<Void> equipoTaskSource = new TaskCompletionSource<>();
    private TaskCompletionSource<Void> ligaTaskSource = new TaskCompletionSource<>();
    public Task<Void> equipoTask = equipoTaskSource.getTask();
    public Task<Void> ligaTask = ligaTaskSource.getTask();

    public EquipoLiga() {
    }

    public EquipoLiga(Equipo equipo, Liga liga, Long partidosGanados, Long partidosEmpatados, Long partidosPerdidos, Long diferenciaGoles, Long puntos) {
        this.equipo = equipo;
        this.liga = liga;
        this.partidosGanados = partidosGanados;
        this.partidosEmpatados = partidosEmpatados;
        this.partidosPerdidos = partidosPerdidos;
        this.partidostotales = partidosGanados+partidosEmpatados+partidosPerdidos;
        this.diferenciaGoles = diferenciaGoles;
        this.puntos = puntos;
    }

    public EquipoLiga(Map<String, Object> datos) {
        if (datos.containsKey("Equipo_ID") && datos.get("Equipo_ID") != null) {
            DocumentReference equipoRef = (DocumentReference) datos.get("Equipo_ID");
            cargarEquipo(equipoRef);
        } else {
            equipoTaskSource.setResult(null);
        }

        if (datos.containsKey("Liga_ID") && datos.get("Liga_ID") != null) {
            DocumentReference ligaRef = (DocumentReference) datos.get("Liga_ID");
            cargarLiga(ligaRef);
        } else {
            ligaTaskSource.setResult(null);
        }

        if (datos.containsKey("Partidos_Empatados") && datos.get("Partidos_Empatados") != null) {
            this.partidosEmpatados = (Long) datos.get("Partidos_Empatados");
        }

        if (datos.containsKey("Partidos_Ganados") && datos.get("Partidos_Ganados") != null) {
            this.partidosGanados = (Long) datos.get("Partidos_Ganados");
        }

        if (datos.containsKey("Partidos_Perdidos") && datos.get("Partidos_Perdidos") != null) {
            this.partidosPerdidos = (Long) datos.get("Partidos_Perdidos");
        }

        if (datos.containsKey("Puntos") && datos.get("Puntos") != null) {
            this.puntos = (Long) datos.get("Puntos");
        }

        this.partidostotales = partidosGanados + partidosEmpatados + partidosPerdidos;
    }

    private void cargarEquipo(DocumentReference equipoRef) {
        equipoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    this.equipo = new Equipo(document.getData());
                }
                equipoTaskSource.setResult(null);
            } else {
                equipoTaskSource.setException(task.getException());
            }
        });
    }

    private void cargarLiga(DocumentReference ligaRef) {
        ligaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    liga = new Liga(document.getData());
                }
                ligaTaskSource.setResult(null);
            } else {
                ligaTaskSource.setException(task.getException());
            }
        });
    }

    public Task<Void> getEquipoTask() {
        return equipoTask;
    }

    public Task<Void> getLigaTask() {
        return ligaTask;
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

    public Long getPartidostotales() {
        return partidostotales;
    }

    public void setPartidostotales(Long partidostotales) {
        this.partidostotales = partidostotales;
    }

    public Long getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(Long partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public Long getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public void setPartidosEmpatados(Long partidosEmpatados) {
        this.partidosEmpatados = partidosEmpatados;
    }

    public Long getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public void setPartidosPerdidos(Long partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    public Long getDiferenciaGoles() {
        return diferenciaGoles;
    }

    public void setDiferenciaGoles(Long diferenciaGoles) {
        this.diferenciaGoles = diferenciaGoles;
    }

    public Long getPuntos() {
        return puntos;
    }

    public void setPuntos(Long puntos) {
        this.puntos = puntos;
    }
}