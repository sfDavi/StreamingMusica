package com.streaming.reproducao.player;

import com.streaming.reproducao.interfaces.ObservadorPlayer;
import com.streaming.reproducao.interfaces.SujeitoPlayer;
import com.streaming.reproducao.enums.StatusReproducao;
import com.streaming.reproducao.models.EstadoPlayer;
import com.streaming.reproducao.models.Musica;
import com.streaming.reproducao.queue.PlayQueue;

import java.util.ArrayList;
import java.util.List;

public class Player implements SujeitoPlayer {

    private EstadoPlayer estado;
    private PlayQueue fila;
    private List<ObservadorPlayer> observadores;
    private Musica musicaAtual;
    private List<Musica> jaTocadas;

    public Player(PlayQueue fila) {
        this.fila = fila;
        this.estado = new EstadoPlayer(null, 0, StatusReproducao.STOP);
        this.observadores = new ArrayList<>();
        this.jaTocadas = new ArrayList<>();
    }

    public void play() {
        if (musicaAtual == null && !fila.estaVazia()) {
            selecionarProximaMusica();
        }
        if (musicaAtual == null) {
            estado = new EstadoPlayer(null, 0, StatusReproducao.STOP);
        } else {
            estado = new EstadoPlayer(musicaAtual.getId(), musicaAtual.getTitulo(), estado.getTempoAtualMs(), StatusReproducao.PLAY);
        }
        notificar();
    }

    public void pause() {
        estado = new EstadoPlayer(
                musicaAtual == null ? null : musicaAtual.getId(),
                musicaAtual == null ? null : musicaAtual.getTitulo(),
                estado.getTempoAtualMs(),
                StatusReproducao.PAUSE
        );
        notificar();
    }

    public void proxima() {
        if (fila.estaVazia()) {
            musicaAtual = null;
            estado = new EstadoPlayer(null, 0, StatusReproducao.STOP);
            notificar();
            return;
        }

        selecionarProximaMusica();
        estado = new EstadoPlayer(musicaAtual.getId(), musicaAtual.getTitulo(), 0, StatusReproducao.PLAY);
        notificar();
    }

    @Override
    public void adicionarObservador(ObservadorPlayer o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    @Override
    public void removerObservador(ObservadorPlayer o) {
        observadores.remove(o);
    }

    @Override
    public void notificar() {
        for (ObservadorPlayer observador : List.copyOf(observadores)) {
            observador.atualizar(estado);
        }
    }

    public EstadoPlayer getEstado() { return estado; }

    public Musica getMusicaAtual() { return musicaAtual; }

    private void selecionarProximaMusica() {
        if (jaTocadas.size() >= fila.tamanho()) {
            jaTocadas.clear();
        }
        Musica proxima = fila.proximaMusica(musicaAtual, jaTocadas);
        if (proxima != null) {
            musicaAtual = proxima;
            jaTocadas.add(proxima);
        }
    }
}
