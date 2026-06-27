package com.streaming.catalogo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo<T> {

    private Map<T, List<T>> adjacencias = new LinkedHashMap<>();

    public void adicionarNo(T no) {
        adjacencias.computeIfAbsent(no, chave -> new ArrayList<>());
    }

    public void adicionarAresta(T origem, T destino) {
        adicionarNo(origem);
        adicionarNo(destino);
        adicionarVizinho(origem, destino);
        adicionarVizinho(destino, origem);
    }

    public List<T> getVizinhos(T no) {
        return Collections.unmodifiableList(adjacencias.getOrDefault(no, List.of()));
    }

    public Set<T> getNos() {
        return Collections.unmodifiableSet(adjacencias.keySet());
    }

    private void adicionarVizinho(T origem, T destino) {
        List<T> vizinhos = adjacencias.get(origem);
        if (!vizinhos.contains(destino)) {
            vizinhos.add(destino);
        }
    }
}
