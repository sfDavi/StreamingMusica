package com.streaming.catalogo.iterators;

import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Musica;

import java.util.List;

public class IteradorLista implements IteradorMusica {

    private List<Musica> faixas;
    private int posicao;

    public IteradorLista(List<Musica> faixas) {
        this.faixas = faixas;
        this.posicao = 0;
    }

    @Override
    public boolean temProximo() { return false; }

    @Override
    public Musica proximo() { return null; }

    @Override
    public void reiniciar() {}
}
