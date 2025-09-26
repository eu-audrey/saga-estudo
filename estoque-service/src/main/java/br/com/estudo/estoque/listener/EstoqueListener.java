package br.com.estudo.estoque.listener;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.estoque.config.EstoqueRabbitMQConfig;
import br.com.estudo.estoque.service.EstoqueService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EstoqueListener {

    private final EstoqueService estoqueService;

    public EstoqueListener(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @RabbitListener(queues = EstoqueRabbitMQConfig.FILA_ESTOQUE)
    public void receberComandoEstoque(PedidoDTO pedidoDTO) {
        System.out.println("\n--- MENSAGEM RECEBIDA: ESTOQUE-SERVICE ---");
        System.out.println("Mensagem recebida na fila " + EstoqueRabbitMQConfig.FILA_ESTOQUE);
        estoqueService.processarEstoque(pedidoDTO);
    }
}
