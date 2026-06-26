package com.streaming.reproducao.strategies;

import com.streaming.reproducao.interfaces.EstrategiaSelecao;
import com.streaming.reproducao.models.Musica;

import java.util.List;

public class EstrategiaSmartMix implements EstrategiaSelecao {

    @Override
    public Musica selecionarProxima(List<Musica> fila, Musica atual, List<Musica> jaTocadas) {
        return null;
    }

    private int distanciaBpm(Musica a, Musica b) { return 0; }
}
