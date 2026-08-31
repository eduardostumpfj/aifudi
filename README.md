# AIFUDI

API REST para o gerenciamento de usuários de uma plataforma compartilhada por restaurantes. Projeto desenvolvido para o Tech Challenge da FIAP - Pós-Tech, módulo 1.

O escopo atual cobre cadastro, autenticação, perfis de acesso e endereços.
## Funcionalidades

- Cadastro de usuários com endereço e os perfis `USER` (cliente) e `OWNER` (dono de restaurante).
- E-mail e login únicos.
- Busca de usuários por nome, com paginação opcional.
- Atualização parcial de dados cadastrais e endereço.
- Troca de senha por rota exclusiva.
- Exclusão de conta.
- Autenticação HTTP Basic com senhas protegidas por BCrypt.
- Controle de acesso: usuários comuns gerenciam a própria conta; `ADMIN` pode alterar ou excluir contas de terceiros.
- Erros padronizados com `ProblemDetail` (RFC 7807).
- Swagger/OpenAPI, coleção Postman, PostgreSQL e Docker Compose.

## Tecnologias

- Java 21 e Spring Boot 4
- Spring Web MVC, JDBC, Validation e Security
- PostgreSQL 16
- springdoc-openapi
- Docker e Docker Compose
- Maven

## Estrutura

```text
.
├── backend/                  # API Spring Boot
├── postgres_db/init.sql      # Schema, triggers, papéis e dados iniciais
├── postman/                  # Coleção de testes Postman
├── docker-compose.yml        # Orquestra backend e PostgreSQL
├── .env.example              # Modelo de variáveis de ambiente
```

## Pré-requisitos

Para executar com containers, instale Docker Engine/Docker Desktop e Docker Compose v2.

Para execução local sem Docker, são necessários Java 21, Maven 3.9+ e PostgreSQL 16.

## Execução com Docker Compose

1. Entre na pasta do projeto:

   ```bash
   cd aifudi
   ```

2. Crie o arquivo de variáveis:

   ```bash
   cp .env.example .env
   ```

3. Preencha `.env`:

   ```dotenv
   POSTGRES_USER=aifudi
   POSTGRES_PASSWORD=troque-esta-senha
   POSTGRES_DB=aifudi
   POSTGRES_PORT=5432
   POSTGRES_HOST=postgres-db

   BACKEND_PORT=8080
   ```

   `POSTGRES_HOST` deve ser `postgres-db`, o nome do serviço no Compose. Mantenha `BACKEND_PORT=8080`, que é a porta padrão da aplicação Spring Boot.

4. Suba os serviços:

   ```bash
   docker compose up --build
   ```

5. Acesse a API em [http://localhost:8080](http://localhost:8080).

Para encerrar:

```bash
docker compose down
```

Para remover também os dados persistidos:

```bash
docker compose down -v
```

> O script de inicialização do banco é executado apenas quando o volume `pgdata` é criado. Use `docker compose down -v` se precisar recriar o banco do zero.

## Documentação da API

Com a aplicação em execução:

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [OpenAPI JSON](http://localhost:8080/v3/api-docs)

Base URL: `http://localhost:8080/v1/users`

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/register` | Não | Cria usuário e endereço |
| `GET` | `?name={nome}&size={tamanho}&page={pagina}` | Basic | Busca usuários por nome |
| `PUT` | `/{login}` | Basic | Atualiza perfil e endereço |
| `PUT` | `/{login}/password` | Basic | Troca a própria senha |
| `DELETE` | `/{login}` | Basic | Exclui a própria conta ou, se ADMIN, outra conta |

## Exemplos

### Cadastro

```bash
curl --request POST http://localhost:8080/v1/users/register \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "José da Silva",
    "email": "jose.silva@example.com",
    "login": "josesilva",
    "password": "senha-segura",
    "roleName": "OWNER",
    "cep": "80000-000",
    "state": "PR",
    "city": "Curitiba",
    "address": "Rua XV de Novembro",
    "number": "111",
    "complement": "Apto 202"
  }'
```

Resposta: `204 No Content`.

Os papéis aceitos no cadastro público são `USER` e `OWNER`. O papel `ADMIN` existe para administração e não pode ser criado por essa rota.

### Busca por nome

```bash
curl --user 'josesilva:senha-segura' \
  'http://localhost:8080/v1/users?name=jose&size=10&page=1'
```

### Atualização de perfil

```bash
curl --request PUT http://localhost:8080/v1/users/josesilva \
  --user 'josesilva:senha-segura' \
  --header 'Content-Type: application/json' \
  --data '{"name":"José da Silva Junior","cep":"80000-001"}'
```

Resposta: `204 No Content`.

### Troca de senha

```bash
curl --request PUT http://localhost:8080/v1/users/josesilva/password \
  --user 'josesilva:senha-segura' \
  --header 'Content-Type: application/json' \
  --data '{"password":"nova-senha-segura"}'
```

## Autenticação e autorização

Rotas protegidas recebem `Authorization: Basic <base64(login:senha)>`. Nos exemplos, `curl --user` monta o cabeçalho automaticamente.

O banco possui contas de desenvolvimento para testes, definidas em `postgres_db/init.sql`. Altere ou remova essas contas antes de uma implantação fora do ambiente local.

## Erros

Erros são retornados no formato `ProblemDetail`. Exemplo para e-mail ou login já existente:

```json
{
  "title": "Duplicate resource",
  "status": 409,
  "detail": "An account with this email already exists",
  "instance": "/v1/users/register"
}
```

| Status | Situação |
|---|---|
| `400` | Corpo ou parâmetros inválidos |
| `401` | Credenciais Basic inválidas |
| `403` | Operação não permitida |
| `404` | Usuário ou papel não encontrado |
| `409` | E-mail ou login já existente |

## Postman

Importe [Aifudi.postman_collection.json](postman/Aifudi.postman_collection.json) no Postman e configure a variável `BASEURL`:

```text
http://localhost:8080/v1/users
```

A coleção inclui cenários de sucesso e erro para cadastro, busca, atualização, senha e exclusão.

## Banco de dados

O schema em [postgres_db/init.sql](postgres_db/init.sql) cria:

- `roles`: papéis `ADMIN`, `USER` e `OWNER`.
- `users`: identidade, credenciais com hash e timestamps.
- `address`: endereço associado a um usuário.

`users.email` e `users.login` possuem restrição `UNIQUE`. A exclusão de usuário remove seus endereços em cascata, e triggers atualizam `updated_at`.

## Execução local sem Docker

1. Crie o banco PostgreSQL e execute `postgres_db/init.sql`.
2. Exporte as variáveis necessárias:

   ```bash
   export POSTGRES_HOST=localhost
   export POSTGRES_PORT=5432
   export POSTGRES_DB=aifudi
   export POSTGRES_USER=aifudi
   export POSTGRES_PASSWORD=troque-esta-senha
   ```

3. Inicie o backend:

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

## Testes

O projeto possui um teste de carregamento do contexto Spring Boot:

```bash
cd backend
./mvnw test
```

Para testes manuais, use a coleção Postman.


## Licença

Projeto acadêmico desenvolvido para o Tech Challenge FIAP.

