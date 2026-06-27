package com.streaming.reproducao.iterators;

import com.streaming.reproducao.interfaces.IteradorMusica;
import com.streaming.reproducao.models.Musica;

import java.util.List;
import java.util.NoSuchElementException;

public class IteradorLista implements IteradorMusica {

    private final List<Musica> faixas;
    private int posicao;

    public IteradorLista(List<Musica> faixas) {
        this.faixas = faixas;
    }

    @Override
    public boolean temProximo() {
        return posicao < faixas.size();
    }

    @Override
    public Musica proximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Nao ha proxima musica na lista local");
        }
        return faixas.get(posicao++);
    }

    @Override
    public void reiniciar() {
        posicao = 0;
    }
}
