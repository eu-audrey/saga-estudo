# Estudo Saga com Orquestração via Mensageria

Este projeto é uma Prova de Conceito (POC) desenvolvida para estudar e demonstrar a implementação do padrão de arquitetura **Saga**, utilizando uma abordagem de **Orquestração** com Spring Boot, Java e **RabbitMQ**.

## 🎯 Sobre o Projeto

O objetivo é simular uma transação de negócio distribuída (uma compra online) que envolve múltiplos microsserviços. A Saga garante que a transação seja concluída com sucesso em todos os serviços ou que seja revertida (compensada) em caso de falha em qualquer etapa. 

Este projeto evoluiu de uma comunicação síncrona (RestTemplate) para uma comunicação **assíncrona** usando um broker de mensagens, o que representa uma arquitetura mais resiliente e escalável, típica de sistemas de microsserviços modernos.

## 🏛️ Arquitetura

O projeto utiliza uma arquitetura de microsserviços com um orquestrador central que se comunica com os demais serviços através de um **Message Broker (RabbitMQ)**.

-   **`orquestrador` (Porta: 8000)**: O cérebro da operação. Ele inicia a saga enviando um evento para a exchange e escuta os eventos de resposta para orquestrar os próximos passos.
-   **`pedido-service` (Porta: 8001)**: Microsserviço especialista em pedidos. Ele escuta os eventos de comando de criação de pedido.
-   **`pagamento-service` (Porta: 8002)**: Microsserviço especialista em pagamentos. (Ainda não migrado para RabbitMQ).
-   **`estoque-service` (Porta: 8003)**: Microsserviço especialista em estoque. (Ainda não implementado).
-   **`common`**: Uma biblioteca compartilhada (JAR) que contém os DTOs e Enums, garantindo um contrato de dados comum entre os serviços.

## 💻 Tecnologias Utilizadas

-   Java 21
-   Spring Boot 3.2.5
-   Apache Maven
-   **RabbitMQ** para comunicação assíncrona (AMQP)
-   **Docker** para rodar a infraestrutura (RabbitMQ)

## 🚀 Como Executar o Projeto

### Pré-requisitos

-   JDK 21 ou superior
-   Apache Maven 3.8+
-   **Docker e Docker Desktop** instalados e em execução.
-   Um cliente de API como o [Postman](https://www.postman.com/downloads/).

### Passos para Execução

1.  **Inicie a Infraestrutura (RabbitMQ)**:
    -   Abra um terminal e execute o seguinte comando Docker para iniciar um container RabbitMQ com a interface de gerenciamento:
    ```sh
    docker run -d --hostname meu-rabbit --name saga-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    ```
    -   Você pode acessar a UI de gerenciamento em `http://localhost:15672` (login: `guest` / `guest`).

2.  **Inicie os Microsserviços Java**:
    -   No seu IDE, inicie os serviços na seguinte ordem (para facilitar a observação dos logs):
        1.  Execute a classe `PedidoApplication.java` (módulo `pedido-service`).
        2.  Execute a classe `OrquestradorApplication.java` (módulo `orquestrador`).

3.  **Dispare a Saga**:
    -   Use o Postman para enviar uma requisição `POST` para o orquestrador.
        -   **URL**: `http://localhost:8000/api/orquestrador/iniciar-pedido`
        -   **Body** (raw, JSON):
        ```json
        {
            "idUsuario": 123,
            "idProduto": 456,
            "descricao": "Teste com RabbitMQ",
            "valor": 99.90
        }
        ```

## ⚙️ O que observar

1.  **Resposta Imediata**: O Postman receberá a resposta `Saga do pedido iniciada!` quase que instantaneamente.
2.  **Console do Orquestrador**: Mostrará que o evento foi enviado para a fila.
3.  **Console do Pedido-Service**: **Alguns instantes depois**, mostrará que a mensagem foi recebida da fila e que a lógica de negócio foi executada.

Este comportamento demonstra o **desacoplamento** e a natureza **assíncrona** da comunicação.

## 🌊 O Fluxo da Saga (Estado Atual)

1.  **Cliente → Orquestrador**: O cliente envia a requisição HTTP para iniciar a saga.
2.  **Orquestrador**: Recebe a requisição, cria um `PedidoDTO` e o publica como um evento na `saga-exchange` com uma routing key específica (`pedido.comando.criar`).
3.  **RabbitMQ**: A exchange roteia a mensagem para a fila `saga-pedido-fila`.
4.  **Pedido-Service**: O `PedidoListener` está escutando a fila, consome a mensagem, e aciona a lógica de negócio no `PedidoService`.
5.  **(Próximo Passo)**: O `pedido-service` precisa enviar um evento de resposta para que o orquestrador saiba que pode continuar para a etapa de pagamento.
