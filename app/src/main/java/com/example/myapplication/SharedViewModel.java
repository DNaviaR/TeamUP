package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.models.Usuario;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<String> nombreLiga = new MutableLiveData<>();
    private final MediatorLiveData<LigaInfo> ligaInfo = new MediatorLiveData<>();

    public SharedViewModel() {
        ligaInfo.addSource(usuario, usuario -> ligaInfo.setValue(new LigaInfo(usuario, nombreLiga.getValue())));
        ligaInfo.addSource(nombreLiga, nombreLiga -> ligaInfo.setValue(new LigaInfo(usuario.getValue(), nombreLiga)));
    }

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

    public MediatorLiveData<LigaInfo> getLigaInfo() {
        return ligaInfo;
    }
}
