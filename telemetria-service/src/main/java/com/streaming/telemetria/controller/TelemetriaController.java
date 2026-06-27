package com.streaming.telemetria.controller;

import com.streaming.telemetria.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/telemetria")
public class TelemetriaController {

    private final List<Map<String, Object>> sincronizacoes = Collections.synchronizedList(new ArrayList<>());

    @PostMapping("/evento")
    public ResponseEntity<?> receberEvento(@RequestBody Map<String, Object> estadoPlayer) {
        return ResponseEntity.accepted().body(registrarSincronizacao(estadoPlayer));
    }

    @RabbitListener(queues = RabbitMqConfig.FILA_TELEMETRIA)
    public void receberEventoBroker(Map<String, Object> estadoPlayer) {
        registrarSincronizacao(estadoPlayer);
    }

    @GetMapping("/sincronizacoes")
    public ResponseEntity<?> listarSincronizacoes() {
        synchronized (sincronizacoes) {
            return ResponseEntity.ok(new ArrayList<>(sincronizacoes));
        }
    }

    @GetMapping("/eventos")
    public ResponseEntity<?> listarEventos() {
        return listarSincronizacoes();
    }

    private Map<String, Object> registrarSincronizacao(Map<String, Object> estadoPlayer) {
        Map<String, Object> sincronizacao = new LinkedHashMap<>();
        sincronizacao.put("estado", new LinkedHashMap<>(estadoPlayer));
        sincronizacao.put("dispositivosSincronizados", List.of("app-celular", "app-smart-tv"));
        sincronizacao.put("recebidoEm", Instant.now().toString());
        sincronizacoes.add(sincronizacao);
        return sincronizacao;
    }
}
