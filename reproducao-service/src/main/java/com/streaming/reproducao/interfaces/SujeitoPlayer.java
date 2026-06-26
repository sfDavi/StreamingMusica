package com.streaming.reproducao.interfaces;

public interface SujeitoPlayer {

    void adicionarObservador(ObservadorPlayer o);

    void removerObservador(ObservadorPlayer o);

    void notificar();
}
