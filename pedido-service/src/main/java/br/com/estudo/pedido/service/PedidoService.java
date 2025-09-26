package br.com.estudo.pedido.service;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import br.com.estudo.pedido.config.PedidoRabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final RabbitTemplate rabbitTemplate;

    public PedidoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processarPedido(PedidoDTO pedidoDTO) {
        System.out.println("\n--- LÓGICA DE NEGÓCIO: PEDIDO-SERVICE ---");
        System.out.println("Pedido recebido da fila: " + pedidoDTO);

        // 1. Simula o salvamento do pedido no banco de dados
        System.out.println("Simulando salvamento do pedido no banco de dados...");

        // 2. Atualiza o status do DTO para indicar sucesso nesta etapa
        pedidoDTO.setStatus(StatusOrquestrador.PEDIDO_CRIADO);

        // 3. Envia a resposta de volta para a exchange da saga
        System.out.println("Enviando resposta de sucesso para o orquestrador...");
        rabbitTemplate.convertAndSend(
                PedidoRabbitMQConfig.EXCHANGE_SAGA,
                PedidoRabbitMQConfig.ROUTING_KEY_RESPOSTA_SUCESSO,
                pedidoDTO
        );

        System.out.println("--- PEDIDO-SERVICE FINALIZADO ---");
    }
}
