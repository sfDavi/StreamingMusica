package com.streaming.historico.controller;

import com.streaming.historico.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    private final List<Map<String, Object>> registros = Collections.synchronizedList(new ArrayList<>());

    @PostMapping("/evento")
    public ResponseEntity<?> receberEvento(@RequestBody Map<String, Object> estadoPlayer) {
        return ResponseEntity.accepted().body(registrarEvento(estadoPlayer));
    }

    @RabbitListener(queues = RabbitMqConfig.FILA_HISTORICO)
    public void receberEventoBroker(Map<String, Object> estadoPlayer) {
        registrarEvento(estadoPlayer);
    }

    @GetMapping("/retrospectiva")
    public ResponseEntity<?> getRetrospectiva() {
        List<Map<String, Object>> snapshot;
        synchronized (registros) {
            snapshot = new ArrayList<>(registros);
        }

        List<String> musicasTocadas = snapshot.stream()
                .map(evento -> Objects.toString(evento.get("tituloAtual"), null))
                .filter(titulo -> titulo != null && !titulo.isBlank())
                .toList();

        Map<String, Long> contagemPorMusica = musicasTocadas.stream()
                .collect(Collectors.groupingBy(titulo -> titulo, LinkedHashMap::new, Collectors.counting()));

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("totalEventos", snapshot.size());
        resposta.put("totalMusicasTocadas", musicasTocadas.size());
        resposta.put("musicasTocadas", musicasTocadas);
        resposta.put("contagemPorMusica", contagemPorMusica);
        resposta.put("ultimoEstado", snapshot.isEmpty() ? null : snapshot.get(snapshot.size() - 1));
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/eventos")
    public ResponseEntity<?> listarEventos() {
        synchronized (registros) {
            return ResponseEntity.ok(new ArrayList<>(registros));
        }
    }

    private Map<String, Object> registrarEvento(Map<String, Object> estadoPlayer) {
        Map<String, Object> registro = new LinkedHashMap<>(estadoPlayer);
        registro.put("recebidoEm", Instant.now().toString());
        registros.add(registro);
        return registro;
    }
}
