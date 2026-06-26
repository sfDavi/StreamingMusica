package com.streaming.catalogo.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo<T> {

    private Map<T, List<T>> adjacencias;

    public void adicionarNo(T no) {}

    public void adicionarAresta(T origem, T destino) {}

    public List<T> getVizinhos(T no) { return null; }

    public Set<T> getNos() { return null; }
}
