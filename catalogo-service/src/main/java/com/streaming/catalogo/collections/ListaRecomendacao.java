package com.streaming.catalogo.collections;

import com.streaming.catalogo.interfaces.ColecaoMusical;
import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.iterators.IteradorGrafo;
import com.streaming.catalogo.models.Grafo;
import com.streaming.catalogo.models.Musica;

public class ListaRecomendacao implements ColecaoMusical {

    private Grafo<Musica> grafoSimilaridade;
    private Musica raiz;

    public ListaRecomendacao(Grafo<Musica> grafoSimilaridade, Musica raiz) {
        this.grafoSimilaridade = grafoSimilaridade;
        this.raiz = raiz;
    }

    @Override
    public IteradorMusica criarIterador() {
        return new IteradorGrafo(grafoSimilaridade, raiz);
    }
}
