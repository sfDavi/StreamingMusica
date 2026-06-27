package com.streaming.reproducao.config;

import com.streaming.reproducao.models.Dispositivo;
import com.streaming.reproducao.models.HistoricoReproducao;
import com.streaming.reproducao.observers.ObservadorHistorico;
import com.streaming.reproducao.observers.ObservadorRabbitMq;
import com.streaming.reproducao.observers.ObservadorTelemetria;
import com.streaming.reproducao.player.Player;
import com.streaming.reproducao.queue.PlayQueue;
import com.streaming.reproducao.strategies.EstrategiaSequencial;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class ReproducaoConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public PlayQueue playQueue() {
        return new PlayQueue(new EstrategiaSequencial());
    }

    @Bean
    public HistoricoReproducao historicoReproducao() {
        return new HistoricoReproducao();
    }

    @Bean
    public ObservadorHistorico observadorHistorico(HistoricoReproducao historicoReproducao) {
        return new ObservadorHistorico(historicoReproducao);
    }

    @Bean
    public ObservadorTelemetria observadorTelemetria() {
        return new ObservadorTelemetria(List.of(
                new Dispositivo("app-celular", "HTTP/SSE"),
                new Dispositivo("app-smart-tv", "WebSocket")
        ));
    }

    @Bean
    public Player player(PlayQueue playQueue,
                         ObservadorTelemetria observadorTelemetria,
                         ObservadorHistorico observadorHistorico,
                         ObservadorRabbitMq observadorRabbitMq) {
        Player player = new Player(playQueue);
        player.adicionarObservador(observadorTelemetria);
        player.adicionarObservador(observadorHistorico);
        player.adicionarObservador(observadorRabbitMq);
        return player;
    }
}
