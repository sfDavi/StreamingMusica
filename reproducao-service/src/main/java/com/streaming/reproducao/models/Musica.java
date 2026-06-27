package com.streaming.reproducao.models;

import java.util.Objects;
import java.util.UUID;

public class Musica {

    private UUID id;
    private String titulo;
    private int duracao;
    private int bpm;

    public Musica() {}

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
    public void setId(UUID id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public void setBpm(int bpm) { this.bpm = bpm; }

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
