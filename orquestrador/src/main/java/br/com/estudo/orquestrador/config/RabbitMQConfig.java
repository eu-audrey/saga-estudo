package br.com.estudo.orquestrador.config;

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
public class RabbitMQConfig {

    // Nome da Exchange
    public static final String EXCHANGE_SAGA = "saga-exchange";

    // --- Routing Keys de Comandos (Enviados pelo Orquestrador) ---
    public static final String ROUTING_KEY_PEDIDOS = "pedido.comando.criar";
    public static final String ROUTING_KEY_PAGAMENTOS = "pagamento.comando.processar";
    public static final String ROUTING_KEY_ESTOQUE = "estoque.comando.separar";

    // --- Filas e Routing Keys de Respostas (Ouvidas pelo Orquestrador) ---
    public static final String FILA_RESPOSTA_ORQUESTRADOR = "saga-orquestrador-resposta-fila";
    public static final String ROUTING_KEY_RESPOSTA_PEDIDO = "pedido.resposta.#";
    public static final String ROUTING_KEY_RESPOSTA_PAGAMENTO = "pagamento.resposta.#";

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(EXCHANGE_SAGA);
    }

    // Bean para a fila de respostas que o orquestrador vai ouvir
    @Bean
    public Queue respostaOrquestradorQueue() {
        return new Queue(FILA_RESPOSTA_ORQUESTRADOR, true);
    }

    // Bean para o vínculo (binding) da fila de respostas de pedido
    @Bean
    public Binding respostaPedidoBinding(TopicExchange exchange, Queue respostaOrquestradorQueue) {
        return BindingBuilder.bind(respostaOrquestradorQueue).to(exchange).with(ROUTING_KEY_RESPOSTA_PEDIDO);
    }

    // Bean para o vínculo (binding) da fila de respostas de pagamento
    @Bean
    public Binding respostaPagamentoBinding(TopicExchange exchange, Queue respostaOrquestradorQueue) {
        return BindingBuilder.bind(respostaOrquestradorQueue).to(exchange).with(ROUTING_KEY_RESPOSTA_PAGAMENTO);
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
