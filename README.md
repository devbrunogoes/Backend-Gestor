# Backend Gestor - Usuários com Permissões

CRUD de usuários com papéis (roles) e permissões, conforme solicitado:
- Administrador: acesso a todos os módulos.
- Gestor / Financeiro: clientes, vendas, financeiro, relatórios.
- Motorista / Vendedor: rotas, vendas, clientes, estoque.
- Usuário Comum: acesso restrito conforme permissões.

## Como está implementado
- Entidades/Enums:
  - `User` (tabela `users`) com campos: `id`, `nome`, `role`, `permissions`.
  - `Role` enum: `ADMINISTRADOR`, `GESTOR`, `FINANCEIRO`, `MOTORISTA`, `VENDEDOR`, `USUARIO_COMUM`.
  - `Permission` enum: `CLIENTES`, `VENDAS`, `FINANCEIRO`, `RELATORIOS`, `ROTAS`, `ESTOQUE`.
- As permissões são salvas em `user_permissions` via `@ElementCollection`.
- Regras de negócio em `UserService`:
  - ADMINISTRADOR: sempre recebe todas as permissões.
  - GESTOR/FINANCEIRO: conjunto fixo {CLIENTES, VENDAS, FINANCEIRO, RELATORIOS}.
  - MOTORISTA/VENDEDOR: conjunto fixo {ROTAS, VENDAS, CLIENTES, ESTOQUE}.
  - USUARIO_COMUM: permissões podem ser customizadas; se não for enviado nada, fica sem permissões.

## Endpoints
Base: `/api/users`

- `GET /api/users` — lista todos os usuários
- `GET /api/users/{id}` — busca um usuário por id
- `POST /api/users` — cria um usuário
- `PUT /api/users/{id}` — atualiza um usuário
- `DELETE /api/users/{id}` — remove um usuário
- `GET /api/users/roles` — lista todos os roles
- `GET /api/users/permissions` — lista todas as permissões
- `GET /api/users/roles/{role}/default-permissions` — permissões padrão de um role

Observação: `{role}` deve ser um dos valores exatos do enum: `ADMINISTRADOR`, `GESTOR`, `FINANCEIRO`, `MOTORISTA`, `VENDEDOR`, `USUARIO_COMUM`.

## Exemplos de payload (JSON)

Criar ADMIN (permissões são ignoradas e será atribuído tudo automaticamente):
```json
{
  "nome": "Ana Admin",
  "role": "ADMINISTRADOR"
}
```

Criar GESTOR (permissões serão automaticamente definidas para o conjunto fixo):
```json
{
  "nome": "Guilherme Gestor",
  "role": "GESTOR"
}
```

Criar VENDEDOR (conjunto fixo):
```json
{
  "nome": "Vera Vendedora",
  "role": "VENDEDOR"
}
```

Criar USUARIO_COMUM com permissões personalizadas:
```json
{
  "nome": "Cecilia Comum",
  "role": "USUARIO_COMUM",
  "permissions": ["CLIENTES", "VENDAS"]
}
```

Atualizar usuário (mesmas regras do create se aplicarão às permissões):
```json
{
  "nome": "Novo Nome",
  "role": "USUARIO_COMUM",
  "permissions": ["CLIENTES"]
}
```

## Como executar
- Banco: PostgreSQL conforme `src/main/resources/application.properties`:
  - `jdbc:postgresql://localhost:5432/sistema_gestor`
  - usuário `postgres` e senha `123456` (ajuste conforme necessário)
  - `spring.jpa.hibernate.ddl-auto=update` cria/atualiza tabelas automaticamente.

- Execução:
  - Pela IDE (IntelliJ): rode a classe `org.backend.BackendApplication`.
  - Via Maven (se instalado no PATH):
    ```cmd
    mvn spring-boot:run
    ```

Se preferir usar artefato:
```cmd
mvn -DskipTests package
java -jar target/backend-gestor-1.0-SNAPSHOT.jar
```

# Backend Gestor - Notas de execução e perfis

## Perfis
- `dev` (desenvolvimento): porta 8082, CORS aberto e autenticação relaxada (permitAll). Use banco local conforme `application-dev.properties`.
- `prod` (produção): porta 8080 por padrão (configurável), autenticação obrigatória para `/api/**` exceto `/api/auth/**`. CORS centralizado; use variáveis de ambiente para banco.
- `staging`: configuração idêntica ao `prod` (autenticação obrigatória para `/api/**` e CORS centralizado), mas normalmente apontando para recursos de homologação (DB, serviços externos). Ative com:

```bash
java -jar -Dspring.profiles.active=staging target/backend-gestor-1.0-SNAPSHOT.jar
```

## Actuator (saúde e métricas)
- `dev`: todos os endpoints expostos (apenas para desenvolvimento).
- `staging`/`prod`: expostos `health`, `info` e `metrics`.
  - Exemplos: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.

## Frontend e Authorization em produção
- O frontend agora envia `Authorization: Bearer <token>` automaticamente apenas em produção (ou quando `window.FORCE_AUTH = true`).
- Heurística de prod: se não for `localhost/127.0.0.1:8082` e existir hostname, considera-se prod. É possível forçar com `window.API_ENV = 'prod'` ou `window.API_ENV = 'dev'`.
- Em dev, nenhum header Authorization é enviado por padrão.

## Swagger/OpenAPI
- Documentação disponível em: `/swagger-ui.html` (ex.: http://localhost:8082/swagger-ui.html no perfil dev).

## Endpoints relevantes
- Autenticação: `POST /api/auth/login`, `POST /api/auth/logout`, `GET /api/auth/me`.
- Configurações do sistema: `GET /api/settings`, `PUT /api/settings`.
- Produtos paginados: `GET /api/products/paged?page=0&size=20&q=texto`.

## Build e execução (Windows cmd)
```bat
cd /d "C:\Users\Bruno Goes\OneDrive\Documentos\Bruno Goes\Projetos Pessoais\Gestor-IA"
mvn\bin\mvn.cmd -DskipTests package
java -jar -Dspring.profiles.active=dev target\backend-gestor-1.0-SNAPSHOT.jar
```

## Testes
- Unitário: `AuthServiceTest` (mockando repositórios).
- Integração: `SettingsControllerIT` (subindo o contexto no perfil `dev`).

Execute:
```bat
mvn\bin\mvn.cmd test
```

## Segurança por perfil
- `dev`: `.anyRequest().permitAll()` para desenvolvimento rápido.
- `prod`: exige Authorization: `Bearer <token>` (sessões via `AuthService`/`UserSessionRepository`). O filtro `TokenAuthFilter` valida token e papel do usuário.

## Observações
- Migrações Flyway aplicadas automaticamente (`db/migration`).
- Configurações de CORS centralizadas em `SecurityConfig`.
- DTOs/validação no endpoint de Settings.
