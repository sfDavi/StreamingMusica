package com.streaming.reproducao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class ReproducaoController {

    @PostMapping("/play")
    public ResponseEntity<?> play() { return null; }

    @PostMapping("/pause")
    public ResponseEntity<?> pause() { return null; }

    @PostMapping("/proxima")
    public ResponseEntity<?> proxima() { return null; }

    @PostMapping("/fila/adicionar")
    public ResponseEntity<?> adicionarColecao(@RequestParam String tipo, @RequestParam String id) { return null; }

    @PutMapping("/estrategia")
    public ResponseEntity<?> setEstrategia(@RequestParam String estrategia) { return null; }
}
