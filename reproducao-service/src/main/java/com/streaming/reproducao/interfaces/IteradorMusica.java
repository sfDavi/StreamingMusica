package com.streaming.reproducao.interfaces;

import com.streaming.reproducao.models.Musica;

public interface IteradorMusica {

    boolean temProximo();

    Musica proximo();

    void reiniciar();
}
