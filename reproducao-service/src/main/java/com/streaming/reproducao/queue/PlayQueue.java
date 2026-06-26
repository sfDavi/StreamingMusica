package com.streaming.reproducao.queue;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.interfaces.IteradorMusica;
import com.streaming.reproducao.models.Musica;

import java.util.ArrayList;
import java.util.List;

public class PlayQueue {

    private List<Musica> fila;
    private EstrategiaSelecao estrategia;

    public PlayQueue(EstrategiaSelecao estrategia) {
        this.fila = new ArrayList<>();
        this.estrategia = estrategia;
    }

    public void adicionarColecao(IteradorMusica it) {}

    public void setEstrategia(EstrategiaSelecao e) { this.estrategia = e; }

    public Musica proximaMusica(Musica atual, List<Musica> jaTocadas) { return null; }

    public boolean estaVazia() { return false; }
}
