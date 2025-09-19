package br.com.estudo.pagamento.service;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.common.enums.StatusOrquestrador;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoService {

    public PedidoDTO processarPagamento(PedidoDTO pedidoDTO) {
        // Simula a lógica de negócio para aprovar ou reprovar o pagamento
        if (pedidoDTO.getValor().compareTo(new BigDecimal("200.00")) <= 0) {
            System.out.println("Pagamento APROVADO para o pedido: " + pedidoDTO.getIdPedido());
            pedidoDTO.setStatus(StatusOrquestrador.SUCESSO);
        } else {
            System.out.println("Pagamento REPROVADO para o pedido: " + pedidoDTO.getIdPedido());
            pedidoDTO.setStatus(StatusOrquestrador.FALHA);
        }
        return pedidoDTO;
    }
}
