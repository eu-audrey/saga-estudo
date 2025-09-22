package br.com.estudo.pagamento.controller;

import br.com.estudo.common.dto.PedidoDTO;
import br.com.estudo.pagamento.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/saude")
    public String healthCheck(){
        return "Pagamento Service está de pé e rodando 🚩🥰!";
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> processarPagamento(@RequestBody PedidoDTO meuPedido){
        System.out.println("Módulo pagamento-service recebeu o PedidoDTO para processar o pagamento:" + meuPedido);
        PedidoDTO pedidoProcessado = pagamentoService.processarPagamento(meuPedido);
        return ResponseEntity.ok(pedidoProcessado);
    }
}
