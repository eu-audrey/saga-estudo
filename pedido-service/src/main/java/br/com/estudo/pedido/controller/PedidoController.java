package br.com.estudo.pedido.controller;

import br.com.estudo.common.dto.PedidoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @GetMapping("/saude")
    public String healthCheck() {
        return "Pedido Service está de pé e rodando 🚩🥰!";
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
        System.out.println("Pedido Service recebeu o PedidoDTO: " + pedidoDTO);
        // Aqui viria a lógica real de persistência do pedido no banco de dados
        // Por enquanto, apenas retornamos o mesmo DTO recebido
        return ResponseEntity.ok(pedidoDTO);
    }
}
