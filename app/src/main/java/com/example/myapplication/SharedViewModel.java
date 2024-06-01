package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.models.Usuario;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<String> nombreLiga = new MutableLiveData<>();

    public void setUsuario(Usuario usuario) {
        this.usuario.setValue(usuario);
    }

    public LiveData<Usuario> getUsuario() {
        return usuario;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga.setValue(nombreLiga);
    }

    public LiveData<String> getNombreLiga() {
        return nombreLiga;
    }
}
