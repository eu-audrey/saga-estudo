package br.com.estudo.orquestrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nome da Exchange
    public static final String EXCHANGE_SAGA = "saga-exchange";

    // Routing Keys (Etiquetas de Roteamento)
    public static final String ROUTING_KEY_PEDIDOS = "pedido.comando.criar";
    public static final String ROUTING_KEY_PAGAMENTOS = "pagamento.comando.processar";
    public static final String ROUTING_KEY_ESTOQUE = "estoque.comando.separar";

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(EXCHANGE_SAGA);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
