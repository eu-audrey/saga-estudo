package br.com.estudo.pagamento.listener;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.pagamento.config.PagamentoRabbitMQConfig;
import br.com.estudo.pagamento.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {

    private final PagamentoService pagamentoService;

    public PagamentoListener(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = PagamentoRabbitMQConfig.FILA_PAGAMENTOS)
    public void receberComandoPagamento(PedidoDTO pedidoDTO) {
        System.out.println("\n--- MENSAGEM RECEBIDA: PAGAMENTO-SERVICE ---");
        System.out.println("Mensagem recebida na fila " + PagamentoRabbitMQConfig.FILA_PAGAMENTOS);
        pagamentoService.processarPagamento(pedidoDTO);
    }
}
