package com.streaming.reproducao.models;

import com.streaming.reproducao.enums.StatusReproducao;

public class EstadoPlayer {

    private String tituloAtual;
    private long tempoAtualMs;
    private StatusReproducao status;

    public EstadoPlayer(String tituloAtual, long tempoAtualMs, StatusReproducao status) {
        this.tituloAtual = tituloAtual;
        this.tempoAtualMs = tempoAtualMs;
        this.status = status;
    }

    public String getTituloAtual() { return tituloAtual; }
    public long getTempoAtualMs() { return tempoAtualMs; }
    public StatusReproducao getStatus() { return status; }
    public void setTituloAtual(String tituloAtual) { this.tituloAtual = tituloAtual; }
    public void setTempoAtualMs(long tempoAtualMs) { this.tempoAtualMs = tempoAtualMs; }
    public void setStatus(StatusReproducao status) { this.status = status; }
}
