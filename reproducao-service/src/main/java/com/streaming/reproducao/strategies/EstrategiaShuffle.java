package com.streaming.reproducao.strategies;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.models.Musica;

import java.util.List;
import java.util.Random;

public class EstrategiaShuffle implements EstrategiaSelecao {

    private Random aleatorio;

    public EstrategiaShuffle() {
        this.aleatorio = new Random();
    }

    @Override
    public Musica selecionarProxima(List<Musica> fila, Musica atual, List<Musica> jaTocadas) {
        if (fila == null || fila.isEmpty()) {
            return null;
        }

        List<Musica> candidatas = fila.stream()
                .filter(musica -> jaTocadas == null || !jaTocadas.contains(musica))
                .toList();
        if (candidatas.isEmpty()) {
            candidatas = fila;
        }
        return candidatas.get(aleatorio.nextInt(candidatas.size()));
    }
}
