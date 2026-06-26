package com.streaming.reproducao.interfaces;

import com.streaming.reproducao.models.EstadoPlayer;

public interface ObservadorPlayer {

    void atualizar(EstadoPlayer estado);
}
