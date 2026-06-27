package com.streaming.reproducao.models;

public class Dispositivo {

    private String identificador;
    private String protocoloDeSincronizacao;
    private EstadoPlayer ultimoEstadoSincronizado;

    public Dispositivo(String identificador, String protocoloDeSincronizacao) {
        this.identificador = identificador;
        this.protocoloDeSincronizacao = protocoloDeSincronizacao;
    }

    public String getIdentificador() { return identificador; }
    public String getProtocoloDeSincronizacao() { return protocoloDeSincronizacao; }
    public EstadoPlayer getUltimoEstadoSincronizado() { return ultimoEstadoSincronizado; }

    public void sincronizar(EstadoPlayer estado) {
        this.ultimoEstadoSincronizado = estado;
    }
}
