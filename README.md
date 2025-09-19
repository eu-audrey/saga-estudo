# Estudo Saga com Orquestração

Este projeto é uma Prova de Conceito (POC) desenvolvida para estudar e demonstrar a implementação do padrão de arquitetura **Saga**, utilizando uma abordagem de **Orquestração** com Spring Boot e Java.

## 🎯 Sobre o Projeto

O objetivo é simular uma transação de negócio distribuída (uma compra online) que envolve múltiplos microsserviços. A Saga garante que a transação seja concluída com sucesso em todos os serviços ou que seja revertida (compensada) em caso de falha em qualquer etapa, mantendo a consistência dos dados.

## 🏛️ Arquitetura

O projeto utiliza uma arquitetura de microsserviços com um orquestrador central, organizada em um projeto multi-módulo Maven.

-   **`orquestrador` (Porta: 8000)**: O cérebro da operação. Ele recebe a requisição inicial e coordena o fluxo, chamando os outros serviços na ordem correta. Não possui lógica de negócio principal.
-   **`pedido-service` (Porta: 8001)**: Microsserviço especialista em tudo relacionado a pedidos.
-   **`pagamento-service` (Porta: 8002)**: Microsserviço especialista em processar pagamentos.
-   **`estoque-service` (Porta: 8003)**: Microsserviço especialista em gerenciar o estoque de produtos (ainda não implementado).
-   **`common`**: Uma biblioteca compartilhada (JAR) que contém os DTOs e Enums utilizados na comunicação entre os serviços, garantindo um "dicionário" comum.

## 💻 Tecnologias Utilizadas

-   Java 21
-   Spring Boot 3.2.5
-   Apache Maven
-   RestTemplate para comunicação síncrona

## 🚀 Como Executar o Projeto

### Pré-requisitos

-   JDK 21 ou superior
-   Apache Maven 3.8+
-   Um cliente de API como o [Postman](https://www.postman.com/downloads/)

### Passos para Execução

Para que a saga funcione, os serviços "operários" devem estar rodando antes do orquestrador ser acionado.

1.  **Inicie o Serviço de Pedidos**:
    -   No seu IDE, execute a classe `PedidoApplication.java` dentro do módulo `pedido-service`.

2.  **Inicie o Serviço de Pagamentos**:
    -   Execute a classe `PagamentoApplication.java` dentro do módulo `pagamento-service`.

3.  **Inicie o Orquestrador**:
    -   Execute a classe `OrquestradorApplication.java` dentro do módulo `orquestrador`.

Ao final, você terá 3 serviços rodando simultaneamente nas portas 8001, 8002 e 8000.

## ⚙️ Como Usar a API

Para iniciar a saga, envie uma requisição `POST` para o orquestrador.

-   **URL**: `http://localhost:8000/api/orquestrador/iniciar-pedido`
-   **Método**: `POST`
-   **Body**: `raw` (JSON)

### Cenário de Sucesso

Para simular um pagamento aprovado, use um valor **menor ou igual a 100.00**.

```json
{
    "idUsuario": 123,
    "idProduto": 456,
    "descricao": "Livro de Arquitetura de Software",
    "valor": 99.90
}
```

### Cenário de Falha

Para simular um pagamento recusado, use um valor **maior que 100.00**.

```json
{
    "idUsuario": 789,
    "idProduto": 101,
    "descricao": "Curso Online Caro",
    "valor": 250.00
}
```

Observe os consoles de cada serviço no seu IDE para acompanhar o fluxo da saga em tempo real.

## 🌊 O Fluxo da Saga (Estado Atual)

1.  **Cliente → Orquestrador**: O cliente envia a requisição com os dados do pedido.
2.  **Orquestrador**: Recebe a requisição, cria um `PedidoDTO` com um `idPedido` único e status `PENDENTE`.
3.  **Orquestrador → Pedido-Service**: Envia o `PedidoDTO` para o serviço de pedidos, que simula a criação do pedido e o retorna.
4.  **Orquestrador → Pagamento-Service**: Após o sucesso da etapa anterior, envia o `PedidoDTO` para o serviço de pagamentos.
5.  **Pagamento-Service**: Aplica a regra de negócio (valor > 100 = FALHA). Atualiza o `status` do `PedidoDTO` para `SUCESSO` ou `FALHA` e o retorna.
6.  **Orquestrador**: Recebe o `PedidoDTO` com o status do pagamento e loga uma mensagem indicando o próximo passo (continuar a saga ou iniciar a compensação).

## 🔮 Próximos Passos

-   [ ] Implementar a etapa de **Estoque** (`estoque-service`).
-   [ ] Implementar a lógica de **Compensação** (rollback) em caso de falha.
-   [ ] Substituir a comunicação síncrona (RestTemplate) por **Mensageria Assíncrona** (ex: RabbitMQ).
-   [ ] Adicionar persistência de dados com **Spring Data JPA** e um banco de dados (ex: H2, PostgreSQL).
-   [ ] Usar **Flyway** para gerenciar as migrações do banco de dados.
