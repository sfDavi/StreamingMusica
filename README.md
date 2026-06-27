# StreamingMusica — Sistema de Streaming de Música

Projeto acadêmico de sistemas distribuídos baseado em microserviços, implementado em **Java com Spring Boot**. A arquitetura aplica três padrões de projeto GoF — **Iterator**, **Strategy** e **Observer** — mapeados diretamente a partir do Diagrama de Classes de Projeto (DCD) produzido na Fase III.

---

## Arquitetura de Microserviços

O sistema é composto por quatro serviços isolados, cada um com sua própria memória (heap JVM), orquestrados via Docker Compose. Toda a persistência de dados ocorre exclusivamente em memória (estruturas `List`, `Map`, `Set`) — nenhum banco de dados é utilizado.

```
docker-compose.yml
broker RabbitMQ      → porta 5672, UI 15672
catalogo-service/    → porta 8081
reproducao-service/  → porta 8082
telemetria-service/  → porta 8083
historico-service/   → porta 8084
```

### Comunicação entre serviços

| Origem | Destino | Tipo | Descrição |
|--------|---------|------|-----------|
| Cliente (app/TV) | `reproducao-service` | REST síncrono | Comandos de play, pause, próxima, troca de estratégia |
| `reproducao-service` | `catalogo-service` | REST síncrono | Busca de coleções e faixas para montar a fila |
| `reproducao-service` | RabbitMQ | AMQP assíncrono | Publicação de mudança de estado do player |
| RabbitMQ | `telemetria-service` | AMQP assíncrono | Sincronização de dispositivos |
| RabbitMQ | `historico-service` | AMQP assíncrono | Registro de eventos para retrospectiva |

---

## Estrutura de Diretórios

```
StreamingMusica/
├── docker-compose.yml                  # Orquestração dos 4 containers e rede interna
│
├── catalogo-service/                   # Microserviço do acervo estático
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/streaming/catalogo/
│       ├── CatalogoApplication.java            # Entry point Spring Boot
│       ├── models/
│       │   ├── Musica.java                     # Entidade central do domínio
│       │   └── Grafo.java                      # Estrutura de grafo para similaridades
│       ├── interfaces/
│       │   ├── ColecaoMusical.java             # Interface fábrica de iteradores
│       │   └── IteradorMusica.java             # Interface do padrão Iterator
│       ├── collections/
│       │   ├── Album.java                      # Coleção linear de faixas
│       │   ├── ListaRecomendacao.java          # Coleção baseada em grafo de similaridade
│       │   └── HistoricoBusca.java             # Coleção baseada em cache de buscas
│       ├── iterators/
│       │   ├── IteradorLista.java              # Percorre List<Musica> sequencialmente
│       │   ├── IteradorGrafo.java              # Percorre Grafo<Musica> via BFS
│       │   └── IteradorCache.java              # Percorre Map<String, Musica>
│       └── controller/
│           └── CatalogoController.java         # Endpoints REST do catálogo
│
├── reproducao-service/                 # Microserviço da sessão dinâmica do usuário
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/streaming/reproducao/
│       ├── ReproducaoApplication.java          # Entry point Spring Boot
│       ├── enums/
│       │   └── StatusReproducao.java           # PLAY | PAUSE | STOP
│       ├── models/
│       │   ├── Musica.java                     # DTO espelho recebido do catalogo-service
│       │   ├── EstadoPlayer.java               # Estado volátil do player (titulo, tempo, status)
│       │   ├── Dispositivo.java                # Representa celular ou TV do usuário
│       │   └── HistoricoReproducao.java        # Registro em memória das músicas tocadas
│       ├── interfaces/
│       │   ├── IteradorMusica.java             # Interface consumidora do padrão Iterator
│       │   ├── EstrategiaSelecao.java          # Interface do padrão Strategy
│       │   ├── SujeitoPlayer.java              # Interface do padrão Observer (sujeito)
│       │   └── ObservadorPlayer.java           # Interface do padrão Observer (observador)
│       ├── strategies/
│       │   ├── EstrategiaSequencial.java       # Algoritmo: ordem de inserção
│       │   ├── EstrategiaShuffle.java          # Algoritmo: aleatório sem repetição
│       │   └── EstrategiaSmartMix.java         # Algoritmo: BPM mais próximo
│       ├── queue/
│       │   └── PlayQueue.java                  # Fila de reprodução (consome IteradorMusica)
│       ├── player/
│       │   └── Player.java                     # Sujeito observável; orquestra fila e estado
│       ├── config/
│       │   ├── ReproducaoConfig.java           # Beans da fila, player e RestTemplate
│       │   └── RabbitMqConfig.java             # Exchange fanout, filas e conversor JSON
│       ├── observers/
│       │   └── ObservadorRabbitMq.java         # Publica eventos de estado no RabbitMQ
│       └── controller/
│           └── ReproducaoController.java       # Endpoints REST do player
│
├── telemetria-service/                 # Microserviço de sincronização de dispositivos
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/streaming/telemetria/
│       ├── TelemetriaApplication.java
│       └── controller/
│           └── TelemetriaController.java       # Recebe eventos e faz push para celular/TV
│
└── historico-service/                  # Microserviço de histórico em tempo real
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/streaming/historico/
        ├── HistoricoApplication.java
        └── controller/
            └── HistoricoController.java        # Recebe eventos e computa retrospectiva
```

---

## Padrões de Projeto GoF

### 1. Iterator (RN01 — A Coleção Dinâmica e Polimórfica)

**Problema:** A fila de reprodução precisa ser alimentada por fontes de dados completamente diferentes — uma lista linear de álbum, um grafo de similaridade e um cache de histórico. O mecanismo que consome a fila deve ser **agnóstico à estrutura de origem**.

**Solução:** O padrão Iterator desacopla o consumidor (`PlayQueue`) das estruturas concretas por meio de uma interface uniforme.

```
ColecaoMusical (interface)
    └── criarIterador() : IteradorMusica
         |
         ├── Album             → cria IteradorLista   (percorre List<Musica>)
         ├── ListaRecomendacao → cria IteradorGrafo   (percorre Grafo<Musica> via BFS)
         └── HistoricoBusca    → cria IteradorCache   (percorre Map<String, Musica>)

IteradorMusica (interface)
    ├── temProximo() : boolean
    ├── proximo()    : Musica
    └── reiniciar()  : void
```

**Fluxo:** O `CatalogoController` expõe as coleções via REST. O `reproducao-service` chama o catálogo, obtém as músicas e cria um `IteradorMusica` local. `PlayQueue.adicionarColecao(IteradorMusica it)` drena o iterador para sua fila interna sem precisar conhecer se as músicas vieram de um álbum, de um grafo ou de um cache.

---

### 2. Strategy (RN02 — A Variação Volátil de Algoritmos)

**Problema:** O critério de seleção da próxima música muda dinamicamente conforme o comando do usuário: sequencial, aleatório ou por proximidade de BPM. Trocar o algoritmo não pode exigir alteração na `PlayQueue`.

**Solução:** O padrão Strategy encapsula cada algoritmo em uma classe concreta intercambiável, injetada na `PlayQueue` em tempo de execução.

```
EstrategiaSelecao (interface)
    └── selecionarProxima(fila, atual, jaTocadas) : Musica
         |
         ├── EstrategiaSequencial  → próxima na ordem de inserção
         ├── EstrategiaShuffle     → aleatória, sem repetir músicas do ciclo atual
         └── EstrategiaSmartMix    → menor distância de BPM em relação à música atual

PlayQueue
    ├── estrategia : EstrategiaSelecao       ← algoritmo atualmente ativo
    ├── setEstrategia(EstrategiaSelecao e)   ← troca em tempo de execução
    └── proximaMusica(atual, jaTocadas)      ← delega à estratégia injetada
```

**Fluxo:** O `ReproducaoController` expõe `PUT /api/player/estrategia`. O usuário envia o modo desejado; o controller instancia a estratégia correspondente e chama `playQueue.setEstrategia(...)`. A partir daí, toda chamada a `proximaMusica()` usa o novo algoritmo — a `PlayQueue` não sabe qual está ativo.

---

### 3. Observer (RN03 — A Reação a Eventos de Estado)

**Problema:** Quando o estado do player muda (play, pause, avanço de música), outros módulos distribuídos precisam ser sincronizados **de forma assíncrona**: o módulo de telemetria (para atualizar celular e TV) e o de histórico (para a retrospectiva).

**Solução:** O padrão Observer desacopla o `Player` (sujeito) dos módulos que reagem à mudança de estado (observadores). Novos observadores podem ser adicionados sem modificar o `Player`.

```
SujeitoPlayer (interface)
    ├── adicionarObservador(ObservadorPlayer o)
    ├── removerObservador(ObservadorPlayer o)
    └── notificar()

Player (implements SujeitoPlayer)
    ├── estado       : EstadoPlayer            ← estado volátil atual
    ├── observadores : List<ObservadorPlayer>  ← lista de inscritos
    ├── play()   → atualiza estado → notificar()
    ├── pause()  → atualiza estado → notificar()
    └── proxima() → atualiza estado → notificar()

ObservadorPlayer (interface)
    └── atualizar(EstadoPlayer estado)
         |
         └── ObservadorRabbitMq → publica evento no exchange fanout player.estado.exchange

RabbitMQ
    ├── telemetria.estado-player.queue → telemetria-service sincroniza celular/TV
    └── historico.estado-player.queue  → historico-service registra evento
```

**Fluxo:** A cada comando do usuário (`play`, `pause`, `proxima`), o `Player` atualiza seu `EstadoPlayer` e chama `notificar()`, que invoca `ObservadorRabbitMq.atualizar(estado)`. O observador publica um evento JSON no exchange fanout `player.estado.exchange`. As filas duráveis `telemetria.estado-player.queue` e `historico.estado-player.queue` ficam vinculadas a esse exchange, permitindo que `telemetria-service` e `historico-service` consumam os eventos de forma assíncrona sem chamadas HTTP entre esses módulos.

---

## Como executar

```bash
docker-compose up --build
```

| Serviço | URL base |
|---------|----------|
| Catálogo | `http://localhost:8081/api/catalogo` |
| Reprodução | `http://localhost:8082/api/player` |
| Telemetria | `http://localhost:8083/api/telemetria` |
| Histórico | `http://localhost:8084/api/historico` |
