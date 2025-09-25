package br.com.estudo.orquestrador.service;

import br.com.estudo.common.dto.OrquestradorRequestDTO;
import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import br.com.estudo.orquestrador.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrquestradorService {

    private final RabbitTemplate rabbitTemplate;

    public OrquestradorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void iniciarSaga(OrquestradorRequestDTO request) {
        System.out.println("\n--- SAGA INICIADA: Enviando evento para criação do pedido ---");

        PedidoDTO pedidoDTO = criarPedidoDTO(request);
        enviarParaFila(RabbitMQConfig.ROUTING_KEY_PEDIDOS, pedidoDTO);

        System.out.println("--- Evento de criação de pedido enviado para a fila. Aguardando respostas... ---");
    }

    private void enviarParaFila(String routingKey, PedidoDTO pedidoDTO) {
        System.out.println("Enviando para a fila com a routing key: " + routingKey);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_SAGA, routingKey, pedidoDTO);
    }

    private PedidoDTO criarPedidoDTO(OrquestradorRequestDTO request) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setIdPedido(UUID.randomUUID().toString());
        pedidoDTO.setIdUsuario(request.getIdUsuario());
        pedidoDTO.setIdProduto(request.getIdProduto());
        pedidoDTO.setDescricao(request.getDescricao());
        pedidoDTO.setValor(request.getValor());
        pedidoDTO.setStatus(StatusOrquestrador.PENDENTE);
        return pedidoDTO;
    }
}
