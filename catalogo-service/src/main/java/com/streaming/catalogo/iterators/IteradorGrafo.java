package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Grafo;
import com.streaming.catalogo.models.Musica;

import java.util.Queue;
import java.util.Set;

public class IteradorGrafo implements IteradorMusica {

    private Grafo<Musica> grafo;
    private Set<Musica> visitados;
    private Queue<Musica> fila;

    public IteradorGrafo(Grafo<Musica> grafo, Musica raiz) {
        this.grafo = grafo;
    }

    @Override
    public boolean temProximo() { return false; }

    @Override
    public Musica proximo() { return null; }

    @Override
    public void reiniciar() {}
}
