package br.com.estudo.pedido.listener;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.pedido.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {

    private final PedidoService pedidoService;

    public PedidoListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "saga-pedido-fila")
    public void receberPedido(PedidoDTO pedidoDTO) {
        System.out.println("\n--- MENSAGEM RECEBIDA: PEDIDO-SERVICE ---");
        System.out.println("Mensagem recebida na fila saga-pedido-fila");
        pedidoService.processarPedido(pedidoDTO);
    }
}
