package com.streaming.reproducao.strategies;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.models.Musica;

import java.util.List;

public class EstrategiaSequencial implements EstrategiaSelecao {

    @Override
    public Musica selecionarProxima(List<Musica> fila, Musica atual, List<Musica> jaTocadas) {
        if (fila == null || fila.isEmpty()) {
            return null;
        }

        List<Musica> candidatas = candidatasDisponiveis(fila, jaTocadas);
        if (candidatas.isEmpty()) {
            return fila.get(0);
        }
        if (atual == null) {
            return candidatas.get(0);
        }

        int posicaoAtual = fila.indexOf(atual);
        if (posicaoAtual < 0) {
            return candidatas.get(0);
        }

        for (int i = 1; i <= fila.size(); i++) {
            Musica candidata = fila.get((posicaoAtual + i) % fila.size());
            if (candidatas.contains(candidata)) {
                return candidata;
            }
        }
        return candidatas.get(0);
    }

    private List<Musica> candidatasDisponiveis(List<Musica> fila, List<Musica> jaTocadas) {
        if (jaTocadas == null || jaTocadas.isEmpty()) {
            return fila;
        }
        return fila.stream().filter(musica -> !jaTocadas.contains(musica)).toList();
    }
}
