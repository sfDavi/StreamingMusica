package com.streaming.reproducao.models;

import java.util.ArrayList;
import java.util.List;

public class HistoricoReproducao {

    private List<Musica> registros;

    public HistoricoReproducao() {
        this.registros = new ArrayList<>();
    }

    public void registrar(EstadoPlayer estado) {}

    public List<Musica> getRegistros() { return registros; }
}
