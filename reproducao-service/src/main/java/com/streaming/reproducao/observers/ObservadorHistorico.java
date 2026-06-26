package com.streaming.reproducao.observers;

import com.streaming.reproducao.interfaces.ObservadorPlayer;
import com.streaming.reproducao.models.EstadoPlayer;
import com.streaming.reproducao.models.HistoricoReproducao;

public class ObservadorHistorico implements ObservadorPlayer {

    private HistoricoReproducao repositorio;

    public ObservadorHistorico(HistoricoReproducao repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void atualizar(EstadoPlayer estado) {}
}
