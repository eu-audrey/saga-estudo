package br.com.estudo.orquestrador.listener;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.orquestrador.config.RabbitMQConfig;
import br.com.estudo.orquestrador.service.OrquestradorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrquestradorListener {

    private final OrquestradorService orquestradorService;

    public OrquestradorListener(OrquestradorService orquestradorService) {
        this.orquestradorService = orquestradorService;
    }

    @RabbitListener(queues = RabbitMQConfig.FILA_RESPOSTA_ORQUESTRADOR)
    public void receberResposta(PedidoDTO pedidoDTO) {
        System.out.println("\n--- RESPOSTA RECEBIDA: ORQUESTRADOR ---");
        System.out.println("Resposta recebida na fila: " + RabbitMQConfig.FILA_RESPOSTA_ORQUESTRADOR);
        System.out.println("Iniciando próxima etapa da saga...");
        orquestradorService.continuarSaga(pedidoDTO);
    }
}
