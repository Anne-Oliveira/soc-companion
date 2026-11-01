# SOC Companion

App iOS (Swift/SwiftUI) com backend em Java (Spring Boot) para
receber eventos de segurança, detectar incidentes e enviar alertas.

## Estrutura
- `backend/` — API Spring Boot
- `ios/` — app SwiftUI
- `docs/` — arquitetura e decisões

## Subindo a infraestrutura local
    cp .env.example .env   # e troque as senhas
    docker compose up -d

RabbitMQ (painel): http://localhost:15672
