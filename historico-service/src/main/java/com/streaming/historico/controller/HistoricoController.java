package com.streaming.historico.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    @PostMapping("/evento")
    public ResponseEntity<?> receberEvento(@RequestBody Map<String, Object> estadoPlayer) { return null; }

    @GetMapping("/retrospectiva")
    public ResponseEntity<?> getRetrospectiva() { return null; }
}
