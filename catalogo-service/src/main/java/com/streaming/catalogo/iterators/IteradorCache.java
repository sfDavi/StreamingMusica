package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Musica;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class IteradorCache implements IteradorMusica {

    private List<Musica> entradas;
    private int posicao;

    public IteradorCache(Map<String, Musica> cache) {
        this.entradas = List.copyOf(cache.values());
        this.posicao = 0;
    }

    @Override
    public boolean temProximo() { return posicao < entradas.size(); }

    @Override
    public Musica proximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Nao ha proxima musica no cache");
        }
        return entradas.get(posicao++);
    }

    @Override
    public void reiniciar() { posicao = 0; }
}
