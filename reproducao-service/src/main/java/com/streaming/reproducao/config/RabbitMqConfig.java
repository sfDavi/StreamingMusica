package com.streaming.reproducao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_ESTADO_PLAYER = "player.estado.exchange";
    public static final String FILA_TELEMETRIA = "telemetria.estado-player.queue";
    public static final String FILA_HISTORICO = "historico.estado-player.queue";

    @Bean
    public FanoutExchange estadoPlayerExchange() {
        return new FanoutExchange(EXCHANGE_ESTADO_PLAYER, true, false);
    }

    @Bean
    public Queue telemetriaQueue() {
        return QueueBuilder.durable(FILA_TELEMETRIA).build();
    }

    @Bean
    public Queue historicoQueue() {
        return QueueBuilder.durable(FILA_HISTORICO).build();
    }

    @Bean
    public Binding telemetriaBinding(Queue telemetriaQueue, FanoutExchange estadoPlayerExchange) {
        return BindingBuilder.bind(telemetriaQueue).to(estadoPlayerExchange);
    }

    @Bean
    public Binding historicoBinding(Queue historicoQueue, FanoutExchange estadoPlayerExchange) {
        return BindingBuilder.bind(historicoQueue).to(estadoPlayerExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
