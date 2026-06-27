# Como Rodar o StreamingMusica

## Requisitos

- Docker Desktop com Docker Compose.
- Portas livres: `5672`, `15672`, `8081`, `8082`, `8083`, `8084`.
- Maven local nao e obrigatorio; os Dockerfiles usam imagem Maven durante o build.

## Subir Todos os Microsservicos

```bash
docker compose up --build
```

Servicos expostos:

| Servico | URL |
| --- | --- |
| RabbitMQ AMQP | `amqp://localhost:5672` |
| RabbitMQ UI | `http://localhost:15672` |
| Catalogo | `http://localhost:8081/api/catalogo` |
| Reproducao | `http://localhost:8082/api/player` |
| Telemetria | `http://localhost:8083/api/telemetria` |
| Historico | `http://localhost:8084/api/historico` |

Credenciais do RabbitMQ UI:

```text
usuario: streaming
senha: streaming
```

## Sequencia Recomendada de Teste

1. Liste albuns: `GET http://localhost:8081/api/catalogo/albuns`.
2. Adicione um album na fila: `POST http://localhost:8082/api/player/fila/adicionar?tipo=album&id=7d4f1d48-9f9e-4d53-b45d-37e04a0c0f01`.
3. Inicie a reproducao: `POST http://localhost:8082/api/player/play`.
4. Avance a musica: `POST http://localhost:8082/api/player/proxima`.
5. Troque a estrategia: `PUT http://localhost:8082/api/player/estrategia?estrategia=smartmix`.
6. Confira telemetria: `GET http://localhost:8083/api/telemetria/sincronizacoes`.
7. Confira historico: `GET http://localhost:8084/api/historico/retrospectiva`.

O arquivo `requests.http` contem a mesma sequencia pronta para clientes HTTP como IntelliJ HTTP Client, VS Code REST Client ou similares.

## Rodar Testes com Docker

Como `mvn` pode nao estar instalado no host, use a imagem Maven:

```bash
docker run --rm -v "%cd%/catalogo-service:/app" -w /app maven:3.9-eclipse-temurin-21 mvn test
docker run --rm -v "%cd%/reproducao-service:/app" -w /app maven:3.9-eclipse-temurin-21 mvn test
docker run --rm -v "%cd%/telemetria-service:/app" -w /app maven:3.9-eclipse-temurin-21 mvn test
docker run --rm -v "%cd%/historico-service:/app" -w /app maven:3.9-eclipse-temurin-21 mvn test
```

No PowerShell, se `%cd%` não expandir, use o caminho absoluto da pasta do projeto.

## Parar Tudo

```bash
docker compose down
```

## Troubleshooting

- Se um serviço não conectar ao RabbitMQ, aguarde o healthcheck do broker ou rode `docker compose restart <servico>`.
- Se alguma porta estiver ocupada, pare o processo usando a porta ou ajuste o mapeamento em `docker-compose.yml`.
- Se o build baixar dependências lentamente, rode novamente; o cache local do Docker tende a acelerar as próximas execuções.
