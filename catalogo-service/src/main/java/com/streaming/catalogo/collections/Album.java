package com.streaming.catalogo.collections;

import com.streaming.catalogo.interfaces.ColecaoMusical;
import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.iterators.IteradorLista;
import com.streaming.catalogo.models.Musica;

import java.util.List;

public class Album implements ColecaoMusical {

    private String id;
    private String titulo;
    private String artista;
    private List<Musica> faixas;

    public Album(String id, String titulo, String artista, List<Musica> faixas) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.faixas = faixas;
    }

    public Album(String titulo, String artista, List<Musica> faixas) {
        this(null, titulo, artista, faixas);
    }

    @Override
    public IteradorMusica criarIterador() {
        return new IteradorLista(faixas);
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public List<Musica> getFaixas() { return faixas; }
}
