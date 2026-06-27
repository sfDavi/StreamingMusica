package com.streaming.reproducao.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoricoReproducao {

    private List<Musica> registros;

    public HistoricoReproducao() {
        this.registros = new ArrayList<>();
    }

    public void registrar(EstadoPlayer estado) {
        if (estado == null || estado.getTituloAtual() == null || estado.getTituloAtual().isBlank()) {
            return;
        }
        registros.add(new Musica(UUID.randomUUID(), estado.getTituloAtual(), 0, 0));
    }

    public List<Musica> getRegistros() { return registros; }
}
