package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Grafo;
import com.streaming.catalogo.models.Musica;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class IteradorGrafo implements IteradorMusica {

    private Grafo<Musica> grafo;
    private Musica raiz;
    private Set<Musica> visitados;
    private Queue<Musica> fila;

    public IteradorGrafo(Grafo<Musica> grafo, Musica raiz) {
        this.grafo = grafo;
        this.raiz = raiz;
        this.visitados = new LinkedHashSet<>();
        this.fila = new ArrayDeque<>();
        reiniciar();
    }

    @Override
    public boolean temProximo() { return !fila.isEmpty(); }

    @Override
    public Musica proximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Nao ha proxima musica no grafo");
        }
        Musica atual = fila.remove();
        for (Musica vizinha : grafo.getVizinhos(atual)) {
            if (visitados.add(vizinha)) {
                fila.add(vizinha);
            }
        }
        return atual;
    }

    @Override
    public void reiniciar() {
        visitados.clear();
        fila.clear();
        if (raiz != null) {
            visitados.add(raiz);
            fila.add(raiz);
        }
    }
}
