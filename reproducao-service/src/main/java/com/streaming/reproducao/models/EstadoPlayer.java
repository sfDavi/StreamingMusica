package com.streaming.reproducao.models;

import com.streaming.reproducao.enums.StatusReproducao;

import java.util.UUID;

public class EstadoPlayer {

    private UUID musicaAtualId;
    private String tituloAtual;
    private long tempoAtualMs;
    private StatusReproducao status;

    public EstadoPlayer() {}

    public EstadoPlayer(String tituloAtual, long tempoAtualMs, StatusReproducao status) {
        this(null, tituloAtual, tempoAtualMs, status);
    }

    public EstadoPlayer(UUID musicaAtualId, String tituloAtual, long tempoAtualMs, StatusReproducao status) {
        this.musicaAtualId = musicaAtualId;
        this.tituloAtual = tituloAtual;
        this.tempoAtualMs = tempoAtualMs;
        this.status = status;
    }

    public UUID getMusicaAtualId() { return musicaAtualId; }
    public String getTituloAtual() { return tituloAtual; }
    public long getTempoAtualMs() { return tempoAtualMs; }
    public StatusReproducao getStatus() { return status; }
    public void setMusicaAtualId(UUID musicaAtualId) { this.musicaAtualId = musicaAtualId; }
    public void setTituloAtual(String tituloAtual) { this.tituloAtual = tituloAtual; }
    public void setTempoAtualMs(long tempoAtualMs) { this.tempoAtualMs = tempoAtualMs; }
    public void setStatus(StatusReproducao status) { this.status = status; }
}
