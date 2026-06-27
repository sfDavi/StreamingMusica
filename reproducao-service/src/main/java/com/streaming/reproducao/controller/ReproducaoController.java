package com.streaming.reproducao.controller;

import com.streaming.reproducao.iterators.IteradorLista;
import com.streaming.reproducao.models.EstadoPlayer;
import com.streaming.reproducao.models.Musica;
import com.streaming.reproducao.player.Player;
import com.streaming.reproducao.queue.PlayQueue;
import com.streaming.reproducao.strategies.EstrategiaSequencial;
import com.streaming.reproducao.strategies.EstrategiaShuffle;
import com.streaming.reproducao.strategies.EstrategiaSmartMix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player")
public class ReproducaoController {

    private final Player player;
    private final PlayQueue playQueue;
    private final RestTemplate restTemplate;
    private final String catalogoUrl;
    private String estrategiaAtual = "sequencial";

    public ReproducaoController(
            Player player,
            PlayQueue playQueue,
            RestTemplate restTemplate,
            @Value("${CATALOGO_URL:http://localhost:8081}") String catalogoUrl
    ) {
        this.player = player;
        this.playQueue = playQueue;
        this.restTemplate = restTemplate;
        this.catalogoUrl = catalogoUrl;
    }

    @PostMapping("/play")
    public ResponseEntity<?> play() {
        player.play();
        return ResponseEntity.ok(respostaEstado());
    }

    @PostMapping("/pause")
    public ResponseEntity<?> pause() {
        player.pause();
        return ResponseEntity.ok(respostaEstado());
    }

    @PostMapping("/proxima")
    public ResponseEntity<?> proxima() {
        player.proxima();
        return ResponseEntity.ok(respostaEstado());
    }

    @PostMapping("/fila/adicionar")
    public ResponseEntity<?> adicionarColecao(@RequestParam String tipo, @RequestParam String id) {
        try {
            List<Musica> musicas = buscarColecao(tipo, id);
            playQueue.adicionarColecao(new IteradorLista(musicas));

            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("tipo", tipo);
            resposta.put("id", id);
            resposta.put("adicionadas", musicas.size());
            resposta.put("totalNaFila", playQueue.tamanho());
            resposta.put("fila", playQueue.getFila());
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Parametros invalidos para a colecao solicitada"));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Colecao nao encontrada no catalogo"));
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("erro", "catalogo-service indisponivel"));
        }
    }

    @PutMapping("/estrategia")
    public ResponseEntity<?> setEstrategia(@RequestParam String estrategia) {
        switch (estrategia.toLowerCase()) {
            case "sequencial" -> playQueue.setEstrategia(new EstrategiaSequencial());
            case "shuffle" -> playQueue.setEstrategia(new EstrategiaShuffle());
            case "smartmix" -> playQueue.setEstrategia(new EstrategiaSmartMix());
            default -> {
                return ResponseEntity.badRequest().body(Map.of("erro", "Estrategia invalida. Use sequencial, shuffle ou smartmix"));
            }
        }
        estrategiaAtual = estrategia.toLowerCase();
        return ResponseEntity.ok(Map.of("estrategia", estrategiaAtual));
    }

    @GetMapping("/estado")
    public ResponseEntity<?> getEstado() {
        return ResponseEntity.ok(respostaEstado());
    }

    @GetMapping("/fila")
    public ResponseEntity<?> getFila() {
        return ResponseEntity.ok(Map.of("total", playQueue.tamanho(), "musicas", playQueue.getFila()));
    }

    private List<Musica> buscarColecao(String tipo, String id) {
        String caminho = switch (tipo.toLowerCase()) {
            case "album", "albuns" -> "/api/catalogo/albuns/" + id + "/faixas";
            case "recomendacao", "recomendacoes" -> "/api/catalogo/recomendacoes/" + id;
            case "historico" -> "/api/catalogo/historico";
            default -> throw new IllegalArgumentException("Tipo de colecao invalido. Use album, recomendacoes ou historico");
        };

        Musica[] musicas = restTemplate.getForObject(catalogoUrl + caminho, Musica[].class);
        if (musicas == null) {
            return List.of();
        }
        return Arrays.asList(musicas);
    }

    private Map<String, Object> respostaEstado() {
        EstadoPlayer estado = player.getEstado();
        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("musicaAtualId", estado.getMusicaAtualId());
        resposta.put("tituloAtual", estado.getTituloAtual());
        resposta.put("tempoAtualMs", estado.getTempoAtualMs());
        resposta.put("status", estado.getStatus());
        resposta.put("estrategia", estrategiaAtual);
        resposta.put("totalNaFila", playQueue.tamanho());
        return resposta;
    }
}
