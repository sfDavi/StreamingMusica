package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Musica;

import java.util.List;
import java.util.NoSuchElementException;

public class IteradorLista implements IteradorMusica {

    private List<Musica> faixas;
    private int posicao;

    public IteradorLista(List<Musica> faixas) {
        this.faixas = faixas;
        this.posicao = 0;
    }

    @Override
    public boolean temProximo() { return posicao < faixas.size(); }

    @Override
    public Musica proximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Nao ha proxima musica na lista");
        }
        return faixas.get(posicao++);
    }

    @Override
    public void reiniciar() { posicao = 0; }
}
