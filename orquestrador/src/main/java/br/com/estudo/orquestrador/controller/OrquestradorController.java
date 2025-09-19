package br.com.estudo.orquestrador.controller;

import br.com.estudo.common.dto.OrquestradorRequestDTO;
import br.com.estudo.orquestrador.service.OrquestradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orquestrador")
public class OrquestradorController {

    @Autowired
    private OrquestradorService orquestradorService;

    @GetMapping("/saude")
    public String healthCheck() {
        return "Orquestrador está de pé e rodando!";
    }

    @PostMapping("/iniciar-pedido")
    public ResponseEntity<?> iniciarPedido(@RequestBody OrquestradorRequestDTO requisicao) {
        orquestradorService.iniciarSaga(requisicao);
        return ResponseEntity.ok("Saga do pedido iniciada!");
    }
}
