package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.Map;

public class Partido {
    private EquipoLiga equipoLocal;
    private EquipoLiga equipoVisitante;
    private Date fecha;
    private int golesLocal;
    private int golesVisitante;
    private String resultado;

    public Partido(EquipoLiga equipoLocal, EquipoLiga equipoVisitante, Date fecha) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fecha = fecha;
        this.golesLocal = 0;
        this.golesVisitante = 0;
        this.resultado = golesLocal+"-"+golesVisitante;
    }

    public Partido(EquipoLiga equipoLocal, EquipoLiga equipoVisitante, Date fecha, String resultado) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fecha = fecha;
        this.resultado = resultado;
    }

    public Partido(Map<String, Object> datos) {
        this.fecha = (Date) datos.get("Fecha");
        if (datos.containsKey("Equipo_Local") && datos.get("Equipo_Local") != null) {
            DocumentReference equipoLocalRef = (DocumentReference) datos.get("Equipo_Local");
            cargarEquipoLocal(equipoLocalRef);
        }
        if (datos.containsKey("Equipo_Visitante") && datos.get("Equipo_Visitante") != null) {
            DocumentReference equipoVisitanteRef = (DocumentReference) datos.get("Equipo_Visitante");
            cargarEquipoVisitante(equipoVisitanteRef);
        }
        if (datos.containsKey("Resultado") && datos.get("Resultado") != null) {
            this.resultado = (String) datos.get("Resultado");
        }
    }

    // Método para cargar el equipo local desde Firestore
    private void cargarEquipoLocal(DocumentReference equipoLocalRef) {
        equipoLocalRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    equipoLocal =  new EquipoLiga(document.getData());
                } else {
                    // Manejar el caso en el que el documento no existe
                }
            } else {
                // Manejar errores
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                exception.printStackTrace();
            }
        });
    }

    // Método para cargar el equipo visitante desde Firestore
    private void cargarEquipoVisitante(DocumentReference equipoVisitanteRef) {
        equipoVisitanteRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    equipoVisitante =  new EquipoLiga(document.getData());
                } else {
                    // Manejar el caso en el que el documento no existe
                }
            } else {
                // Manejar errores
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                exception.printStackTrace();
            }
        });
    }

    // Getters y Setters
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EquipoLiga getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(EquipoLiga equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public EquipoLiga getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(EquipoLiga equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
