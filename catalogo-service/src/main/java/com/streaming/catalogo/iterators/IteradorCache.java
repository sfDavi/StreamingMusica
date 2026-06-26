package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Musica;

import java.util.List;
import java.util.Map;

public class IteradorCache implements IteradorMusica {

    private List<Musica> entradas;
    private int posicao;

    public IteradorCache(Map<String, Musica> cache) {
        this.entradas = List.copyOf(cache.values());
        this.posicao = 0;
    }

    @Override
    public boolean temProximo() { return false; }

    @Override
    public Musica proximo() { return null; }

    @Override
    public void reiniciar() {}
}
