package br.com.estudo.pedido.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoRabbitMQConfig {

    // Nomes da infraestrutura (devem ser exatamente os mesmos do orquestrador)
    public static final String FILA_PEDIDOS = "saga-pedido-fila";
    public static final String EXCHANGE_SAGA = "saga-exchange";
    public static final String ROUTING_KEY_PEDIDOS = "pedido.comando.criar";

    @Bean
    public Queue pedidosQueue() {
        return new Queue(FILA_PEDIDOS, true);
    }

    @Bean
    public Binding pedidosBinding(TopicExchange exchange) {
        return BindingBuilder.bind(pedidosQueue()).to(exchange).with(ROUTING_KEY_PEDIDOS);
    }

    @Bean
    public TopicExchange sagaExchange() {
        // Declara a exchange aqui também para garantir que ela exista, caso este serviço suba primeiro.
        // O Spring é inteligente e não vai recriar se já existir.
        return new TopicExchange(EXCHANGE_SAGA);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
