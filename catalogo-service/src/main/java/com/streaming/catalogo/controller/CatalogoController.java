package com.streaming.catalogo.controller;

import com.streaming.catalogo.collections.Album;
import com.streaming.catalogo.collections.HistoricoBusca;
import com.streaming.catalogo.collections.ListaRecomendacao;
import com.streaming.catalogo.interfaces.IteradorMusica;
import com.streaming.catalogo.models.Grafo;
import com.streaming.catalogo.models.Musica;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final Map<String, Album> albuns = new LinkedHashMap<>();
    private final Map<UUID, Musica> musicas = new LinkedHashMap<>();
    private final Map<String, Musica> historicoBusca = new LinkedHashMap<>();
    private final Grafo<Musica> grafoSimilaridade = new Grafo<>();

    public CatalogoController() {
        carregarAcervo();
    }

    @GetMapping("/albuns")
    public ResponseEntity<?> listarAlbuns() {
        List<Map<String, Object>> resposta = albuns.values().stream()
                .map(album -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", album.getId());
                    item.put("titulo", album.getTitulo());
                    item.put("artista", album.getArtista());
                    item.put("totalFaixas", album.getFaixas().size());
                    return item;
                })
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/albuns/{id}/faixas")
    public ResponseEntity<?> getFaixasAlbum(@PathVariable String id) {
        Album album = albuns.get(id);
        if (album == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drenar(album.criarIterador()));
    }

    @GetMapping("/recomendacoes/{musicaId}")
    public ResponseEntity<?> getRecomendacoes(@PathVariable String musicaId) {
        UUID id;
        try {
            id = UUID.fromString(musicaId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "musicaId deve ser um UUID valido"));
        }

        Musica raiz = musicas.get(id);
        if (raiz == null) {
            return ResponseEntity.notFound().build();
        }

        ListaRecomendacao recomendacao = new ListaRecomendacao(grafoSimilaridade, raiz);
        return ResponseEntity.ok(drenar(recomendacao.criarIterador()));
    }

    @GetMapping("/historico")
    public ResponseEntity<?> getHistoricoBusca() {
        HistoricoBusca historico = new HistoricoBusca(historicoBusca);
        return ResponseEntity.ok(drenar(historico.criarIterador()));
    }

    @GetMapping("/musicas")
    public ResponseEntity<?> listarMusicas() {
        return ResponseEntity.ok(musicas.values());
    }

    private List<Musica> drenar(IteradorMusica iterador) {
        List<Musica> resultado = new ArrayList<>();
        while (iterador.temProximo()) {
            resultado.add(iterador.proximo());
        }
        return resultado;
    }

    private void carregarAcervo() {
        String ledZeppelinIvId = "7d4f1d48-9f9e-4d53-b45d-37e04a0c0f01";
        String backInBlackId = "69e42a7c-4f88-465a-8df4-2e2f060cb102";
        String appetiteForDestructionId = "0af5e09b-7a8b-4e3c-b95c-d7b4a92b6c03";

        Musica blackDog = musica("f0d2ee63-5e93-4a43-9685-049a9e77f101", "Black Dog", 296, 82);
        Musica rockAndRoll = musica("943d7b3a-a0e9-4c5d-98cb-987d0f68f102", "Rock and Roll", 220, 170);
        Musica stairwayToHeaven = musica("1fc8da99-0f3a-4e8a-8f2f-264064a18f03", "Stairway to Heaven", 482, 82);
        Musica whenTheLeveeBreaks = musica("0b740a97-d7f0-49d7-bb7a-ecb579b9f104", "When the Levee Breaks", 427, 72);

        Musica hellsBells = musica("3ef91845-f90d-4fb5-a41c-9d6050c0a201", "Hells Bells", 312, 106);
        Musica shootToThrill = musica("ebc37fb6-0f27-4a50-af77-5850ed355202", "Shoot to Thrill", 317, 141);
        Musica backInBlack = musica("b87e2ccf-90dc-44fd-91d4-50e7eece5203", "Back in Black", 255, 94);
        Musica youShookMeAllNightLong = musica("317cbad7-d884-4f66-91f2-b2b1f9775204", "You Shook Me All Night Long", 210, 127);

        Musica welcomeToTheJungle = musica("23b0589e-3479-43d0-a238-a120d9176301", "Welcome to the Jungle", 273, 123);
        Musica sweetChildOMine = musica("e5d7b772-d897-44df-a9c7-5fa74d777302", "Sweet Child O' Mine", 356, 125);
        Musica paradiseCity = musica("bcf45421-6e26-4a42-8755-c327f2397303", "Paradise City", 406, 100);
        Musica nightrain = musica("e7e4ce95-5bbd-4935-a103-9a8d31547304", "Nightrain", 266, 149);

        albuns.put(ledZeppelinIvId, new Album(ledZeppelinIvId, "Led Zeppelin IV", "Led Zeppelin", List.of(blackDog, rockAndRoll, stairwayToHeaven, whenTheLeveeBreaks)));
        albuns.put(backInBlackId, new Album(backInBlackId, "Back in Black", "AC/DC", List.of(hellsBells, shootToThrill, backInBlack, youShookMeAllNightLong)));
        albuns.put(appetiteForDestructionId, new Album(appetiteForDestructionId, "Appetite for Destruction", "Guns N' Roses", List.of(welcomeToTheJungle, sweetChildOMine, paradiseCity, nightrain)));

        historicoBusca.put("hard rock", blackDog);
        historicoBusca.put("rock anos 80", backInBlack);
        historicoBusca.put("guitar heroes", sweetChildOMine);

        grafoSimilaridade.adicionarAresta(stairwayToHeaven, blackDog);
        grafoSimilaridade.adicionarAresta(blackDog, rockAndRoll);
        grafoSimilaridade.adicionarAresta(stairwayToHeaven, whenTheLeveeBreaks);
        grafoSimilaridade.adicionarAresta(blackDog, sweetChildOMine);
        grafoSimilaridade.adicionarAresta(sweetChildOMine, paradiseCity);
        grafoSimilaridade.adicionarAresta(backInBlack, hellsBells);
        grafoSimilaridade.adicionarAresta(backInBlack, shootToThrill);
        grafoSimilaridade.adicionarAresta(backInBlack, welcomeToTheJungle);
    }

    private Musica musica(String id, String titulo, int duracao, int bpm) {
        Musica musica = new Musica(UUID.fromString(id), titulo, duracao, bpm);
        musicas.put(musica.getId(), musica);
        return musica;
    }
}
