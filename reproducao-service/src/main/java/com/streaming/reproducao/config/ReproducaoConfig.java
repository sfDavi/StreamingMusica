package com.streaming.reproducao.config;

import com.streaming.reproducao.observers.ObservadorRabbitMq;
import com.streaming.reproducao.player.Player;
import com.streaming.reproducao.queue.PlayQueue;
import com.streaming.reproducao.strategies.EstrategiaSequencial;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
    public Player player(PlayQueue playQueue, ObservadorRabbitMq observadorRabbitMq) {
        Player player = new Player(playQueue);
        player.adicionarObservador(observadorRabbitMq);
        return player;
    }
}
