package com.streaming.catalogo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @GetMapping("/albuns")
    public ResponseEntity<?> listarAlbuns() { return null; }

    @GetMapping("/albuns/{id}/faixas")
    public ResponseEntity<?> getFaixasAlbum(@PathVariable String id) { return null; }

    @GetMapping("/recomendacoes/{musicaId}")
    public ResponseEntity<?> getRecomendacoes(@PathVariable String musicaId) { return null; }

    @GetMapping("/historico")
    public ResponseEntity<?> getHistoricoBusca() { return null; }
}
