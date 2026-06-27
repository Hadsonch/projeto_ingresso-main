# PRD - API Gestão de Ingressos para Eventos Culturais

## Problema original
Sistema Java Spring Boot para venda de ingressos com Usuario↔Endereco (OneToOne), Evento↔Local (ManyToOne), Ingresso como transação central, regras de negócio (capacidade do local, limite de 5/usuário/evento), métricas in-memory, JWT, Swagger.

## Arquitetura
- Java 21 + Spring Boot 3.3.4 + PostgreSQL + Gradle 8.10
- Camadas: Controllers → Services → Repositories → Models (Entities + DTOs)
- DTOs separados: *FormDTO (entrada, validados) e *ResponseDTO (saída)
- Segurança JWT stateless (HS256) com SecurityConfig + SecurityFilter + TokenService
- Tratamento global de erros via @RestControllerAdvice
- Documentação: springdoc-openapi (Swagger UI em /swagger-ui.html)

## Implementado (2026-02)
- Entidades: Endereco, Usuario, Local, Evento, Ingresso (+ enum TipoUsuario)
- Repositories JPA com queries customizadas (countByEventoId, countByUsuarioIdAndEventoId)
- Services com @Transactional e regras de negócio
- Controllers REST: Auth, Usuario, Local, Evento, Ingresso
- Validações Jakarta: @NotBlank, @Email, @Size, @Future, @DecimalMin, @Min
- 7 critérios obrigatórios atendidos:
  1. OneToOne Usuario↔Endereco
  2. ManyToOne Evento↔Local, Ingresso↔Usuario, Ingresso↔Evento
  3. Ingresso como entidade de transação central
  4. Validação de capacidade + limite 5/usuario/evento (validarRegras)
  5. Endpoint GET /ingressos/resumo (cálculo via streams)
  6. JWT Security (SecurityConfig + TokenService) + login + cadastro público
  7. Swagger/OpenAPI com bearerAuth configurado

## Endpoints validados manualmente
- POST /usuarios (público) ✅
- POST /auth/login ✅ (e 401 em senha inválida)
- POST/GET /locais, /eventos, /ingressos com Bearer ✅
- GET /ingressos/meus, /ingressos/resumo ✅
- Regra capacidade (4ª compra em local com cap=3 → 400) ✅
- Regra limite 5/usuário/evento (6ª compra → 400) ✅
- Validação Jakarta retorna 400 com lista de field errors ✅
- 404 em recurso inexistente ✅
- Sem token → 403 ✅

## Como rodar
- PostgreSQL local na porta 5432 (db `ingressos_db`, user `ingressos`, senha `ingressos123`)
- `cd /app/spring-backend && gradle bootJar && java -jar build/libs/app.jar`
- App roda em http://localhost:8090 (porta 8080 estava em uso por outro processo no container)
- Swagger UI: http://localhost:8090/swagger-ui.html

## Backlog / Next Actions
- P1: Adicionar paginação em listagens (Pageable do Spring Data)
- P1: Frontend React consumindo a API (caso seja necessário no futuro)
- P2: Adicionar endpoint PUT/DELETE para Locais/Eventos/Usuários
- P2: Testes unitários (JUnit 5) e de integração (@SpringBootTest)
- P2: Refresh tokens / logout
- P2: Cancelamento de ingressos com regras de reembolso
- P2: Notificação por e-mail após compra
