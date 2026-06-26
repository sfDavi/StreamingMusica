package com.streaming.reproducao.observers;

import com.streaming.reproducao.interfaces.ObservadorPlayer;
import com.streaming.reproducao.models.Dispositivo;
import com.streaming.reproducao.models.EstadoPlayer;

import java.util.List;

public class ObservadorTelemetria implements ObservadorPlayer {

    private List<Dispositivo> dispositivos;

    public ObservadorTelemetria(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    @Override
    public void atualizar(EstadoPlayer estado) {}
}
