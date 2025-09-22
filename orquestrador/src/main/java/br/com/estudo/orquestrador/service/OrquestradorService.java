package br.com.estudo.orquestrador.service;

import br.com.estudo.common.dto.OrquestradorRequestDTO;
import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class OrquestradorService {

    private final RestTemplate restTemplate;

    private static final String PEDIDO_SERVICE_URL = "http://localhost:8001/api/pedidos";
    private static final String PAGAMENTO_SERVICE_URL = "http://localhost:8002/api/pagamentos";

    public OrquestradorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void iniciarSaga(OrquestradorRequestDTO request) {
        System.out.println("\n--- INICIANDO SAGA PARA O PEDIDO: " + request + " ---");

        // ETAPA 1: Criação do Pedido
        String idPedido = UUID.randomUUID().toString();
        PedidoDTO pedidoDTO = criarPedidoDTO(request, idPedido);

        System.out.println("ETAPA 1: Enviando para pedido-service...");
        PedidoDTO pedidoCriado = restTemplate.postForObject(PEDIDO_SERVICE_URL, pedidoDTO, PedidoDTO.class);
        System.out.println("Retorno do pedido-service: " + pedidoCriado);

        // ETAPA 2: Processamento do Pagamento
        System.out.println("\nETAPA 2: Enviando para pagamento-service...");
        PedidoDTO pedidoComPagamento = restTemplate.postForObject(PAGAMENTO_SERVICE_URL, pedidoCriado, PedidoDTO.class);
        System.out.println("Retorno do pagamento-service: " + pedidoComPagamento);

        // Lógica de decisão baseada no status do pagamento
        if (StatusOrquestrador.SUCESSO.equals(pedidoComPagamento.getStatus())) {
            System.out.println("\nSUCESSO: Pagamento aprovado. Continuando a saga...");
            // Próximo passo seria chamar o estoque-service
        } else {
            System.out.println("\nFALHA: Pagamento recusado. Iniciando compensação...");
            // Próximo passo seria chamar a compensação do pedido-service
        }

        System.out.println("--- FIM DA SAGA ---");
    }

    private PedidoDTO criarPedidoDTO(OrquestradorRequestDTO request, String idPedido) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setIdPedido(idPedido);
        pedidoDTO.setIdUsuario(request.getIdUsuario());
        pedidoDTO.setIdProduto(request.getIdProduto());
        pedidoDTO.setDescricao(request.getDescricao()); // <-- ADICIONADO AQUI
        pedidoDTO.setValor(request.getValor());
        pedidoDTO.setStatus(StatusOrquestrador.PENDENTE);
        return pedidoDTO;
    }
}
