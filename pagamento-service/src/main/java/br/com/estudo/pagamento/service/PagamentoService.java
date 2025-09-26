package br.com.estudo.pagamento.service;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import br.com.estudo.pagamento.config.PagamentoRabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoService {

    private final RabbitTemplate rabbitTemplate;

    public PagamentoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processarPagamento(PedidoDTO pedidoDTO) {
        System.out.println("\n--- LÓGICA DE NEGÓCIO: PAGAMENTO-SERVICE ---");
        System.out.println("Pagamento recebido para o pedido: " + pedidoDTO.getIdPedido());

        // Lógica de negócio para aprovar ou reprovar o pagamento
        if (pedidoDTO.getValor().compareTo(new BigDecimal("100.00")) > 0) {
            System.out.println("Pagamento RECUSADO para o pedido " + pedidoDTO.getIdPedido() + ". Valor acima de 100.00.");
            pedidoDTO.setStatus(StatusOrquestrador.FALHA);
            enviarResposta(PagamentoRabbitMQConfig.ROUTING_KEY_RESPOSTA_FALHA, pedidoDTO);
        } else {
            System.out.println("Pagamento APROVADO para o pedido " + pedidoDTO.getIdPedido() + ".");
            pedidoDTO.setStatus(StatusOrquestrador.PAGAMENTO_APROVADO);
            enviarResposta(PagamentoRabbitMQConfig.ROUTING_KEY_RESPOSTA_SUCESSO, pedidoDTO);
        }
        System.out.println("--- PAGAMENTO-SERVICE FINALIZADO ---");
    }

    private void enviarResposta(String routingKey, PedidoDTO pedidoDTO) {
        System.out.println("Enviando resposta com a routing key: " + routingKey);
        rabbitTemplate.convertAndSend(
                PagamentoRabbitMQConfig.EXCHANGE_SAGA,
                routingKey,
                pedidoDTO
        );
    }
}
