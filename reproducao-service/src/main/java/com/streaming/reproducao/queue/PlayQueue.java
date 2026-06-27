package com.streaming.reproducao.queue;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.interfaces.IteradorMusica;
import com.streaming.reproducao.models.Musica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayQueue {

    private List<Musica> fila;
    private EstrategiaSelecao estrategia;

    public PlayQueue(EstrategiaSelecao estrategia) {
        this.fila = new ArrayList<>();
        this.estrategia = estrategia;
    }

    public void adicionarColecao(IteradorMusica it) {
        while (it.temProximo()) {
            fila.add(it.proximo());
        }
    }

    public void setEstrategia(EstrategiaSelecao e) { this.estrategia = e; }

    public Musica proximaMusica(Musica atual, List<Musica> jaTocadas) {
        if (estrategia == null) {
            return null;
        }
        return estrategia.selecionarProxima(fila, atual, jaTocadas);
    }

    public boolean estaVazia() { return fila.isEmpty(); }

    public int tamanho() { return fila.size(); }

    public List<Musica> getFila() { return Collections.unmodifiableList(fila); }
}
