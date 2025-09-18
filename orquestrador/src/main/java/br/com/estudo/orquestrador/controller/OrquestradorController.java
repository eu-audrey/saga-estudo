package br.com.estudo.orquestrador.controller;

import br.com.estudo.common.dto.OrquestradorRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orquestrador")
public class OrquestradorController {

    @GetMapping("/saude")
    public String healthCheck() {
        return "Orquestrador is up and running!";
    }

    @PostMapping("/iniciar-pedido")
    public ResponseEntity<?> iniciarPedido(@RequestBody OrquestradorRequestDTO request) {
        // Lógica para iniciar a saga virá aqui
        System.out.println("Recebido pedido para iniciar saga: " + request);
        return ResponseEntity.ok("Saga do pedido iniciada!");
    }
}
