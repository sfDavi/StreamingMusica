package com.streaming.reproducao.interfaces;

import com.streaming.reproducao.models.Musica;

import java.util.List;

public interface EstrategiaSelecao {

    Musica selecionarProxima(List<Musica> fila, Musica atual, List<Musica> jaTocadas);
}
