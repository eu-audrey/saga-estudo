package br.com.estudo.estoque.service;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import br.com.estudo.estoque.config.EstoqueRabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final RabbitTemplate rabbitTemplate;

    public EstoqueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processarEstoque(PedidoDTO pedidoDTO) {
        System.out.println("\n--- LÓGICA DE NEGÓCIO: ESTOQUE-SERVICE ---");
        System.out.println("Requisição para separação de estoque recebida para o pedido: " + pedidoDTO.getIdPedido());

        // Simula a lógica de separação de estoque
        // Para simplificar, vamos assumir que sempre há estoque.
        System.out.println("Estoque para o produto " + pedidoDTO.getIdProduto() + " separado com sucesso.");

        // Atualiza o status do DTO para indicar sucesso nesta etapa
        // Vamos usar o status SUCESSO por enquanto, indicando o fim da saga.
        pedidoDTO.setStatus(StatusOrquestrador.SUCESSO);

        // Envia a resposta de volta para a exchange da saga
        System.out.println("Enviando resposta de sucesso para o orquestrador...");
        rabbitTemplate.convertAndSend(
                EstoqueRabbitMQConfig.EXCHANGE_SAGA,
                EstoqueRabbitMQConfig.ROUTING_KEY_RESPOSTA_SUCESSO,
                pedidoDTO
        );

        System.out.println("--- ESTOQUE-SERVICE FINALIZADO ---");
    }
}
