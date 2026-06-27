package com.streaming.reproducao.observers;

import com.streaming.reproducao.config.RabbitMqConfig;
import com.streaming.reproducao.interfaces.ObservadorPlayer;
import com.streaming.reproducao.models.EstadoPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ObservadorRabbitMq implements ObservadorPlayer {

    private static final Logger log = LoggerFactory.getLogger(ObservadorRabbitMq.class);

    private final RabbitTemplate rabbitTemplate;

    public ObservadorRabbitMq(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void atualizar(EstadoPlayer estado) {
        Map<String, Object> evento = new LinkedHashMap<>();
        evento.put("musicaAtualId", estado.getMusicaAtualId());
        evento.put("tituloAtual", estado.getTituloAtual());
        evento.put("tempoAtualMs", estado.getTempoAtualMs());
        evento.put("status", estado.getStatus().name());
        evento.put("timestamp", Instant.now().toString());
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_ESTADO_PLAYER, "", evento);
        } catch (AmqpException e) {
            log.warn("Falha ao publicar evento do player no RabbitMQ: {}", e.getMessage());
        }
    }
}
