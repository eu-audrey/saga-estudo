package br.com.estudo.orquestrador.service;

import br.com.estudo.common.dto.OrquestradorRequestDTO;
import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class OrquestradorService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PEDIDO_SERVICE_URL = "http://localhost:8001/api/pedidos";

    public void iniciarSaga(OrquestradorRequestDTO request) {
        System.out.println("LOGICA DA SAGA INICIADA NO SERVICE PARA O PEDIDO: " + request);

        // 1. Gerar um ID único para o pedido
        String idPedido = UUID.randomUUID().toString();

        // 2. Criar o PedidoDTO com status inicial PENDENTE
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setIdPedido(idPedido);
        pedidoDTO.setIdUsuario(request.getIdUsuario());
        pedidoDTO.setIdProduto(request.getIdProduto());
        pedidoDTO.setValor(request.getValor());
        pedidoDTO.setStatus(StatusOrquestrador.PENDENTE);

        System.out.println("Enviando PedidoDTO para pedido-service: " + pedidoDTO);

        // 3. Chamar o pedido-service para criar o pedido
        // O pedido-service retornará o PedidoDTO atualizado (com o mesmo ID e status, por enquanto)
        PedidoDTO pedidoCriado = restTemplate.postForObject(PEDIDO_SERVICE_URL, pedidoDTO, PedidoDTO.class);

        System.out.println("Retorno do pedido-service: " + pedidoCriado);

        // A lógica de continuar a saga (pagamento, estoque) virá aqui
    }
}
