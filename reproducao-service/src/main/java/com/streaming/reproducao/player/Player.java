package com.streaming.reproducao.player;

import com.streaming.reproducao.interfaces.ObservadorPlayer;
import com.streaming.reproducao.interfaces.SujeitoPlayer;
import com.streaming.reproducao.models.EstadoPlayer;
import com.streaming.reproducao.queue.PlayQueue;

import java.util.ArrayList;
import java.util.List;

public class Player implements SujeitoPlayer {

    private EstadoPlayer estado;
    private PlayQueue fila;
    private List<ObservadorPlayer> observadores;

    public Player(PlayQueue fila) {
        this.fila = fila;
        this.observadores = new ArrayList<>();
    }

    public void play() {}

    public void pause() {}

    public void proxima() {}

    @Override
    public void adicionarObservador(ObservadorPlayer o) {}

    @Override
    public void removerObservador(ObservadorPlayer o) {}

    @Override
    public void notificar() {}
}
