package com.streaming.reproducao.strategies;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.models.Musica;

import java.util.List;

public class EstrategiaSmartMix implements EstrategiaSelecao {

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
        if (atual == null) {
            return candidatas.get(0);
        }

        return candidatas.stream()
                .min((a, b) -> Integer.compare(distanciaBpm(atual, a), distanciaBpm(atual, b)))
                .orElse(null);
    }

    private int distanciaBpm(Musica a, Musica b) { return Math.abs(a.getBpm() - b.getBpm()); }
}
