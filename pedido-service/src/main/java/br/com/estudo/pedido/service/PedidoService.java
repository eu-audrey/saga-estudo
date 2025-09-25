package br.com.estudo.pedido.service;

import br.com.estudo.common.dto.PedidoDTO;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    public void processarPedido(PedidoDTO pedidoDTO) {
        System.out.println("\n--- LÓGICA DE NEGÓCIO: PEDIDO-SERVICE ---");
        System.out.println("Pedido recebido da fila: " + pedidoDTO);
        System.out.println("Simulando salvamento do pedido no banco de dados...");
        System.out.println("--- PEDIDO-SERVICE FINALIZADO ---");

        // No futuro, aqui enviaremos a resposta de volta para a fila do orquestrador
    }
}
