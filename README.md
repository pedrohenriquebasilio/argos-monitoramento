# Prova Técnica - Desenvolvedor Backend

## Descrição do Projeto

Este projeto é uma API RESTful desenvolvida em Spring Boot 3, inspirada no JSONPlaceholder, com autenticação JWT e documentação Swagger/OpenAPI. O objetivo é demonstrar habilidades em desenvolvimento backend seguro, seguindo boas práticas de código e arquitetura do padrão DDD.

### Estrutura de Pastas e Arquivos

- **src/main/java/argos/com/br/blog/application/**: Serviços de aplicação e regras de negócio.
- **src/main/java/argos/com/br/blog/domain/**: Entidades e interfaces do domínio.
- **src/main/java/argos/com/br/blog/infrastructure/config/**: Configurações gerais, incluindo OpenAPI/Swagger.
- **src/main/java/argos/com/br/blog/infrastructure/security/**: Configuração de segurança, JWT e filtros.
- **src/main/java/argos/com/br/blog/presentation/**: Controllers REST para cada recurso (posts, comments, albums, photos, todos, users, auth).
- **src/main/resources/application.properties**: Configurações da aplicação, banco de dados e Swagger.
- **src/main/resources/db/migration/**: Scripts de migração do banco de dados (Flyway).
- **src/test/java/argos/com/br/blog/**: Testes unitários e de integração.
- **pom.xml**: Dependências Maven e plugins do projeto.

## Funcionalidades Implementadas

- **Autenticação e Autorização**:
  - Registro de usuários (signup) e login.
  - Geração de tokens JWT para autenticação.
  - Proteção de endpoints sensíveis, liberando apenas para usuários autenticados.
- **Recursos da API**:
  - **/posts**: CRUD completo, relacionamento com comentários.
  - **/comments**: CRUD completo, relacionamento com postagens.
  - **/albums**: CRUD completo, relacionamento com fotos.
  - **/photos**: CRUD completo, relacionamento com álbuns.
  - **/todos**: CRUD completo, atribuição a usuários.
  - **/users**: CRUD completo, campos como nome, email, etc.
- **Documentação**: Swagger/OpenAPI disponível para todos os endpoints.
- **Testes**: Testes unitários e de integração para os principais fluxos.
- **Boas Práticas**: Clean Code, SOLID, commits frequentes e claros.

## Como Executar Localmente

1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd crud-basico
   ```
2. Configure o banco de dados (MySQL/PostgreSQL). Para testes rápidos, utilize o NeonDB (instância temporária de 7 dias):
   - [https://neon.tech/](https://neon.tech/) (crie uma instância gratuita e temporária)
   - Atualize o `application.properties` com as credenciais fornecidas pelo NeonDB.
3. Execute as migrações:
   ```bash
   ./mvnw flyway:migrate
   ```
4. Inicie a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Acesse a documentação Swagger:
   - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Disponíveis

- **Autenticação**: `/auth/signup`, `/auth/login`
- **Posts**: `/posts`, `/posts/{id}`
- **Comments**: `/comments`, `/comments/{id}`
- **Albums**: `/albums`, `/albums/{id}`
- **Photos**: `/photos`, `/photos/{id}`
- **Todos**: `/todos`, `/todos/{id}`
- **Users**: `/users`, `/users/{id}`

Consulte o Swagger para detalhes de cada endpoint, parâmetros e exemplos de uso.

## Autenticação JWT

- Após o login, um token JWT é retornado.
- Para acessar endpoints protegidos, envie o token no header:
  ```
  Authorization: Bearer <seu_token_jwt>
  ```
- Endpoints públicos: `/auth/**`, `/swagger-ui.html`, `/api-docs/**`

## Deploy Online

O projeto pode ser testado online por até **duas semanas (14 dias)** após o deploy. Para acessar a API hospedada, utilize:

```bash
curl https://<SUA-URL-DEPLOY>.vercel.app/api/posts
```

> **Banco NeonDB:**
> - O banco utilizado é uma instância temporária do NeonDB, válida por 7 dias. Após esse período, será necessário criar uma nova instância e atualizar as credenciais.

## Swagger Online

A documentação Swagger estará disponível online (se hospedado) por até 14 dias:

- [https://<SUA-URL-DEPLOY>.vercel.app/swagger-ui.html](https://<SUA-URL-DEPLOY>.vercel.app/swagger-ui.html)

## Possíveis Melhorias Futuras

- Implementar refresh token para autenticação.
- Adicionar paginação e filtros avançados nos endpoints.
- Melhorar cobertura de testes.
- Deploy automatizado (CI/CD).
- Monitoramento e métricas.

---

> Projeto desenvolvido para avaliação técnica. Dúvidas ou sugestões, entre em contato!
