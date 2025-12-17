Objetivo
========
Escreva um front-end simples em HTML/CSS/JS (sem frameworks) que se integre com o back-end existente (API REST) para autenticação e CRUD de usuários com roles/permissions.

Plano de ação (o que o arquivo descreve)
----------------------------------------
- Checklist de arquivos e dependências necessárias.
- Contrato da API (endpoints e formatos esperados).
- Estrutura de pastas e arquivos sugeridos.
- Código de exemplo (JS) para: wrapper de API (fetch), autenticação (login/logout), carga e render de usuários, criação/edição.
- Regras de UI/UX: autorização por permissões/roles, validações, tratamento de erros.
- Comandos para servir arquivos estáticos e testar localmente.
- Observações de segurança (CORS, armazenamento de token, XSS).

Checklist mínimo
-----------------
- HTML pages: `login.html`, `index.html` (dashboard), `users.html` (lista + formulário).
- CSS: `css/styles.css` (layout e responsividade básica).
- JS: `js/api.js`, `js/auth.js`, `js/ui.js`, `js/users.js`.
- Servir arquivos estáticos (mesmo domínio ideal; caso contrário configurar CORS no backend).

Estrutura sugerida de arquivos
------------------------------
frontend/
  index.html
  login.html
  users.html
  css/
    styles.css
  js/
    api.js
    auth.js
    ui.js
    users.js
  assets/

Contrato de API (endpoints)
---------------------------
A API esperada (base: `http://localhost:8080/api`) deve expor:

- POST `/api/auth/login`
  - Request JSON: `{ "email": "...", "password": "..." }`
  - Response JSON: `{ "token": "<token>", "userId": number, "nome": string, "email": string, "role": string, "permissions": string[], "expiresAt": string }

- (Opcional) POST `/api/auth/logout` — para invalidar token no servidor.

- GET `/api/users` — lista todos os usuários.
- GET `/api/users/{id}`
- POST `/api/users` — cria usuário: `{ nome, email, password, role, permissions? }`
- PUT `/api/users/{id}` — atualiza usuário.
- PATCH `/api/users/{id}/active?active=false` — ativa/desativa.
- PUT `/api/users/{id}/permissions` — atualiza permissões (body: array strings).
- DELETE `/api/users/{id}`
- GET `/api/users/roles` — lista enum `Role`.
- GET `/api/users/permissions` — lista enum `Permission`.
- GET `/api/users/roles/{role}/default-permissions` — permissões padrão por role.

Observação: confirmar o path exato do controller de autenticação no backend; `AuthController` pode usar `/api/auth/login`.

Shapes (exemplos de DTOs)
-------------------------
- LoginRequest: `{ email: string, password: string }`
- AuthResponse: `{ token: string, userId: number, nome: string, email: string, role: string, permissions: string[], expiresAt: string }`
- User (entrada/saída): `{ id?, nome, email, password?, role, permissions?: string[], active?: boolean }`

Como autenticar e armazenar token (recomendação)
------------------------------------------------
Fluxo recomendado:
1. Login envia POST `/api/auth/login`.
2. Backend retorna `AuthResponse` com `token` e `permissions`.
3. Salvar `token`, `user` (perfil) e `expiresAt` em `localStorage` (ou `sessionStorage`).
4. Criar wrapper de fetch que injete `Authorization: Bearer <token>` em todas requisições.
5. Detectar 401 em respostas e forçar logout (limpar storage e redirect para login).

Nota de segurança: `localStorage` é simples mas vulnerável a XSS. Em produção prefira cookie `httpOnly` (requer alteração no backend).

Código de exemplo: wrapper de API (js)
-------------------------------------
// arquivo: `js/api.js`
```javascript
const API_BASE = 'http://localhost:8080/api'; // ajustar conforme ambiente

function getToken() {
  return localStorage.getItem('token');
}

async function apiFetch(path, options = {}) {
  const headers = options.headers ? { ...options.headers } : {};
  const token = getToken();
  if (token) headers['Authorization'] = 'Bearer ' + token;
  if (!headers['Content-Type'] && !(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }

  const res = await fetch(API_BASE + path, { ...options, headers });

  if (res.status === 401) {
    // logout global
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('expiresAt');
    window.location.href = '/login.html';
    throw new Error('Unauthorized');
  }

  const text = await res.text();
  const data = text ? JSON.parse(text) : null;

  if (!res.ok) {
    const err = new Error((data && data.message) || res.statusText);
    err.status = res.status;
    err.body = data;
    throw err;
  }
  return data;
}
```

Código de exemplo: autenticação (js)
-----------------------------------
// arquivo: `js/auth.js`
```javascript
async function login(email, password) {
  const body = { email, password };
  const data = await apiFetch('/auth/login', { method: 'POST', body: JSON.stringify(body) });
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify({ id: data.userId, nome: data.nome, email: data.email, role: data.role, permissions: data.permissions }));
  localStorage.setItem('expiresAt', data.expiresAt);
  return data;
}

function logout() {
  // opcional: chamar endpoint de logout no backend
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  localStorage.removeItem('expiresAt');
  window.location.href = '/login.html';
}

function getCurrentUser() {
  const s = localStorage.getItem('user');
  return s ? JSON.parse(s) : null;
}

function isLoggedIn() {
  const t = localStorage.getItem('token');
  const exp = localStorage.getItem('expiresAt');
  if (!t) return false;
  if (exp && new Date(exp) < new Date()) { logout(); return false; }
  return true;
}
```

Protegendo páginas (on load)
----------------------------
Em cada página protegida (por exemplo `users.html`), adicione no topo do script:
```javascript
if (!isLoggedIn()) window.location.href = '/login.html';
const currentUser = getCurrentUser();
```

Exemplo de renderização da tabela de usuários
--------------------------------------------
// arquivo: `js/users.js`
```javascript
function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/[&<>"']/g, function (m) { return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'})[m]; });
}

async function loadUsers() {
  try {
    const users = await apiFetch('/users'); // GET /api/users
    renderUsersTable(users);
  } catch (e) {
    showError(e.message || 'Erro ao carregar usuários');
  }
}

function renderUsersTable(users) {
  const tbody = document.querySelector('#usersTable tbody');
  tbody.innerHTML = '';
  users.forEach(u => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${u.id}</td>
      <td>${escapeHtml(u.nome)}</td>
      <td>${escapeHtml(u.email)}</td>
      <td>${u.role}</td>
      <td>${u.active ? 'Sim' : 'Não'}</td>
      <td>
        <button data-id="${u.id}" class="edit">Editar</button>
        <button data-id="${u.id}" class="delete">Excluir</button>
      </td>`;
    tbody.appendChild(tr);
  });
}
```

Criar/Editar usuário
---------------------
- Formulário com campos: `nome`, `email`, `password` (apenas para criar/alterar senha), `role` (select), `permissions` (multi-select).
- Carregar opções de `role` com `GET /api/users/roles` e `permissions` com `GET /api/users/permissions`.
- No submit:
  - POST `/api/users` para criar
  - PUT `/api/users/{id}` para atualizar

Exemplo de payload de criação
```json
{
  "nome": "Ana",
  "email": "ana@exemplo",
  "password": "senha123",
  "role": "USUARIO_COMUM",
  "permissions": ["ROTAS","VENDAS"]
}
```

Gerenciamento de permissões na UI
----------------------------------
- Salvar `permissions` e `role` no `localStorage` durante login.
- Helpers:
```javascript
function hasPermission(p) {
  const u = getCurrentUser();
  return u && u.permissions && u.permissions.includes(p);
}
function hasRole(r) { const u = getCurrentUser(); return u && u.role === r; }
```
- Usar essas funções para mostrar/esconder elementos e habilitar ações.

Validações (lado cliente)
-------------------------
- Nome: obrigatório.
- Email: obrigatório, regex simples.
- Senha: para criação obrigatório (mínimo 6~8 chars); para edição opcional.
- Role: obrigatório.
- Mostrar erros de validação antes de enviar a requisição.
- Também tratar erros retornados pelo backend (400, 409, etc).

Tratamento de erros da API
--------------------------
- No `apiFetch`, transformar respostas não-ok em exceções com `status` e `body`.
- Mostrar mensagens amigáveis (ex.: `Email já cadastrado` para 409).
- Spinner/loading durante operações.

CORS e configuração do backend
------------------------------
- Se front e back estiverem em domínios diferentes durante desenvolvimento (ex.: front em :5173 e backend :8080), garantir que `CorsConfig` do backend permita o origin do front e o header `Authorization`.
- Em produção, preferível servir arquivos estáticos no mesmo domínio do backend para evitar CORS.

Servir arquivos estáticos localmente (comandos)
----------------------------------------------
Usando Python (rápido):
```cmd
# abra um terminal na pasta frontend/
python -m http.server 5173
# abra http://localhost:5173/login.html
```
Usando `serve` (Node):
```cmd
npm install -g serve
serve -l 5173 .
```

Testes manuais mínimos
----------------------
1. Abrir `login.html` e efetuar login com admin (`admin@local` / `admin123`).
2. Acessar `users.html` e verificar a lista.
3. Criar um usuário com `USUARIO_COMUM` e permissões customizadas.
4. Editar e desativar um usuário (PATCH `/active`).
5. Testar logout e confirmar que páginas protegidas redirecionam para `login.html`.

Boas práticas de segurança
--------------------------
- Não exibir `passwordHash` vindo do servidor.
- Evitar `innerHTML` com dados sem escape (usar `escapeHtml`).
- Implementar Content Security Policy (CSP) em produção.
- Preferir cookie `httpOnly` para token em produção (requer backend);
- Usar HTTPS em produção.

Melhorias futuras e extras
--------------------------
- Paginação e filtros no endpoint `GET /api/users` se houver muitos usuários.
- Confirmações de ação (delete/desativar).
- Testes automatizados: unitários (Jest), E2E (Cypress).
- Integração com servidor de arquivos (servir frontend pelo Spring Boot em produção).

Próximo passo (opções que eu posso executar para você)
-----------------------------------------------------
- Gerar o esqueleto completo dos arquivos HTML/CSS/JS (login.html, users.html, `js/api.js`, `js/auth.js`, `js/users.js`, `css/styles.css`).
- Gerar apenas os arquivos JS essenciais para colar no seu projeto.
- Gerar exemplos menores (apenas login + listagem) para teste rápido.

Escolha uma opção e eu gero os arquivos prontos.

