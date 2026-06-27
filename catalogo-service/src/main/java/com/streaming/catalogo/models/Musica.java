package com.streaming.catalogo.models;

import java.util.UUID;
import java.util.Objects;

public class Musica {

    private UUID id;
    private String titulo;
    private int duracao;
    private int bpm;

    public Musica(UUID id, String titulo, int duracao, int bpm) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.bpm = bpm;
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getDuracao() { return duracao; }
    public int getBpm() { return bpm; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Musica musica)) return false;
        return Objects.equals(id, musica.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
