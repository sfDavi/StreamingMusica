package com.streaming.catalogo.collections;

import com.streaming.catalogo.interfaces.ColecaoMusical;
import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.iterators.IteradorLista;
import com.streaming.catalogo.models.Musica;

import java.util.List;

public class Album implements ColecaoMusical {

    private String titulo;
    private String artista;
    private List<Musica> faixas;

    public Album(String titulo, String artista, List<Musica> faixas) {
        this.titulo = titulo;
        this.artista = artista;
        this.faixas = faixas;
    }

    @Override
    public IteradorMusica criarIterador() {
        return new IteradorLista(faixas);
    }
}
