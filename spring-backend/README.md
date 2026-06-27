# API Gestão de Ingressos - Eventos Culturais

Sistema de venda de ingressos para eventos com Java 21 + Spring Boot 3.3 + PostgreSQL + JWT + Swagger.

## Stack
- **Java 21**, **Gradle 8.10**, **Spring Boot 3.3.4**
- **PostgreSQL** (driver `org.postgresql`)
- **Spring Security + JWT** (`com.auth0:java-jwt:4.4.0`)
- **springdoc-openapi 2.6** (Swagger UI)
- **Lombok**, **Jakarta Validation**

## Estrutura
```
src/main/java/com/eventos/ingressos/
├── config/         OpenApiConfig
├── controller/     Auth, Usuario, Local, Evento, Ingresso
├── dto/            *FormDTO (entrada) e *ResponseDTO (saída)
├── exception/      GlobalExceptionHandler, RegraNegocio, RecursoNaoEncontrado
├── model/          Endereco, Usuario, Local, Evento, Ingresso, TipoUsuario
├── repository/     JpaRepositories
├── security/       SecurityConfig, SecurityFilter, TokenService
└── service/        UsuarioService, AuthService, LocalService, EventoService, IngressoService
```

## Relacionamentos
- **Usuario ↔ Endereco**: `@OneToOne` (cascade ALL, orphanRemoval)
- **Usuario ↔ Ingresso**: `@ManyToOne` em Ingresso
- **Evento ↔ Local**: `@ManyToOne` (entidade Local com capacidade)
- **Ingresso ↔ Evento + Usuario**: entidade de transação central

## Regras de Negócio (`IngressoService.validarRegras`)
1. Não emitir ingresso se `count(ingressos do evento) >= capacidadeMaxima do Local`.
2. Limite de **5 ingressos por usuário** para o mesmo evento.
3. Email único para cadastro de usuário.

## Endpoint estatístico in-memory
`GET /ingressos/resumo` — calcula via streams Java:
- `totalIngressosVendidos`
- `receitaTotal` (soma de `valorPago`)
- `taxaOcupacaoMedia` (média de `vendidos/capacidade` por evento)
- `eventoMaisVendidoNome` / `Id` / `Qtd`

## Segurança
- Rotas públicas: `POST /usuarios`, `POST /auth/login`, `/swagger-ui/**`, `/v3/api-docs/**`
- Demais rotas exigem header `Authorization: Bearer <TOKEN>`
- JWT stateless (HS256), expiração configurável

## Tratamento de erros (`@RestControllerAdvice`)
- `400 Bad Request` — `MethodArgumentNotValidException` + `RegraNegocioException`
- `404 Not Found` — `RecursoNaoEncontradoException`
- `401 Unauthorized` — `BadCredentialsException`
- `403 Forbidden` — `AccessDeniedException`

## Como rodar

### Pré-requisitos
- Java 21+
- PostgreSQL rodando (default: `localhost:5432`, db `ingressos_db`, user `ingressos`, senha `ingressos123`)

### Criar banco
```bash
sudo -u postgres psql -c "CREATE USER ingressos WITH PASSWORD 'ingressos123' SUPERUSER;"
sudo -u postgres psql -c "CREATE DATABASE ingressos_db OWNER ingressos;"
```

### Variáveis de ambiente (opcionais)
```
DB_URL=jdbc:postgresql://localhost:5432/ingressos_db
DB_USER=ingressos
DB_PASSWORD=ingressos123
SERVER_PORT=8090
JWT_SECRET=my-secret-jwt-key-change-me-in-production-please-use-strong-key
JWT_EXPIRATION_HOURS=8
```

### Build & run
```bash
cd /app/spring-backend
gradle build -x test
java -jar build/libs/app.jar
```

Aplicação sobe em `http://localhost:8090`.

### Swagger UI
- http://localhost:8090/swagger-ui.html
- OpenAPI JSON: http://localhost:8090/v3/api-docs

## Endpoints principais

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/usuarios` | Pública | Cadastra usuário (+ endereço) |
| POST | `/auth/login` | Pública | Login → JWT |
| GET  | `/usuarios/{id}` | Bearer | Busca usuário |
| GET  | `/locais` | Bearer | Lista locais |
| POST | `/locais` | Bearer | Cria local |
| GET  | `/locais/{id}` | Bearer | Busca local |
| GET  | `/eventos` | Bearer | Lista eventos |
| POST | `/eventos` | Bearer | Cria evento |
| GET  | `/eventos/{id}` | Bearer | Busca evento |
| POST | `/ingressos` | Bearer | Compra ingresso |
| GET  | `/ingressos` | Bearer | Lista todos |
| GET  | `/ingressos/meus` | Bearer | Lista do usuário logado |
| GET  | `/ingressos/resumo` | Bearer | Estatísticas in-memory |

## Exemplo rápido (curl)

```bash
# 1. Cadastrar
curl -X POST http://localhost:8090/usuarios -H "Content-Type: application/json" -d '{
  "nome":"Alice","email":"alice@ex.com","senha":"senha123","tipo":"CLIENTE",
  "endereco":{"logradouro":"Rua A","numero":"100","bairro":"Centro","cidade":"SP","uf":"SP","cep":"01000-000"}
}'

# 2. Login
TOKEN=$(curl -s -X POST http://localhost:8090/auth/login -H "Content-Type: application/json" \
  -d '{"email":"alice@ex.com","senha":"senha123"}' | jq -r .token)

# 3. Criar local + evento + comprar
curl -X POST http://localhost:8090/locais -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"nome":"Teatro","cidade":"SP","uf":"SP","capacidadeMaxima":100}'

curl -X POST http://localhost:8090/eventos -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"nome":"Show","descricao":"Banda X","dataHora":"2027-12-31T20:00:00","precoIngresso":150.00,"localId":1}'

curl -X POST http://localhost:8090/ingressos -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"eventoId":1}'

# 4. Resumo
curl -X GET http://localhost:8090/ingressos/resumo -H "Authorization: Bearer $TOKEN"
```
