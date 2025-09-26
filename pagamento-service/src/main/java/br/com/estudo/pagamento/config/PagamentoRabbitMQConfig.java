package br.com.estudo.pagamento.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoRabbitMQConfig {

    // Nomes da infraestrutura
    public static final String FILA_PAGAMENTOS = "saga-pagamento-fila";
    public static final String EXCHANGE_SAGA = "saga-exchange";
    public static final String ROUTING_KEY_PAGAMENTOS = "pagamento.comando.processar";

    // --- Novas Routing Keys para a Resposta ---
    public static final String ROUTING_KEY_RESPOSTA_SUCESSO = "pagamento.resposta.sucesso";
    public static final String ROUTING_KEY_RESPOSTA_FALHA = "pagamento.resposta.falha";

    @Bean
    public Queue pagamentosQueue() {
        return new Queue(FILA_PAGAMENTOS, true);
    }

    @Bean
    public Binding pagamentosBinding(TopicExchange exchange) {
        return BindingBuilder.bind(pagamentosQueue()).to(exchange).with(ROUTING_KEY_PAGAMENTOS);
    }

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
