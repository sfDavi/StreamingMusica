package com.streaming.catalogo.collections;

import com.streaming.catalogo.interfaces.ColecaoMusical;
import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.iterators.IteradorCache;
import com.streaming.catalogo.models.Musica;

import java.util.Map;

public class HistoricoBusca implements ColecaoMusical {

    private Map<String, Musica> cache;

    public HistoricoBusca(Map<String, Musica> cache) {
        this.cache = cache;
    }

    @Override
    public IteradorMusica criarIterador() {
        return new IteradorCache(cache);
    }
}
