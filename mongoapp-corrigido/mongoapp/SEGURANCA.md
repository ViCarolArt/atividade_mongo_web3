# Correções de segurança aplicadas

| # | Falha | Correção |
|---|-------|----------|
| 1 | Credenciais no `application.properties` | URI e senhas via variáveis de ambiente (`.env.example`); `.gitignore` reforçado; `target/` fora do pacote |
| 2 | Atlas liberado para 0.0.0.0/0 | Configuração no Atlas (ver abaixo) |
| 3 | Usuário `admin`/`root` com privilégio total | Configuração no Atlas (ver abaixo) |
| 4 | API sem autenticação/autorização | Spring Security (Basic + BCrypt), perfis ADMIN/USER, PUT/DELETE só ADMIN |
| 5 | XSS no `index.html` | `textContent` + `addEventListener`, JS/CSS externos, CSP restritiva |
| 6 | Mass assignment / sobrescrita via `id` | DTOs `UsuarioRequest`/`UsuarioResponse` |
| 7 | Sem validação | Bean Validation + índice único de e-mail + normalização |
| 8 | HTTP e URL fixa | URL relativa; HSTS; instruções de TLS |
| 9 | `findAll()` sem paginação | Paginação com tamanho máximo 50 |
| 10 | Erros vazando detalhes | `GlobalExceptionHandler` (404/409/400/500 genéricos) |
| 11 | CORS/headers/CSRF | Mesma origem, CSRF por cookie, CSP, X-Frame-Options, Referrer-Policy |
| 12 | Sem logs | Log de criação/alteração/remoção (apenas `id`, sem dados pessoais) |

## Como rodar
```bash
export MONGODB_URI='mongodb+srv://USUARIO_APP:SENHA@cluster.mongodb.net/fatecbd?retryWrites=true&w=majority'
export APP_ADMIN_PASSWORD='senha-longa-com-12+-caracteres'
export APP_USER_PASSWORD='outra-senha-longa-12+'
./mvnw spring-boot:run
```
Acesse http://localhost:8080 e entre com `admin` (todas as ações) ou `operador` (listar/criar).

## Passos manuais no MongoDB Atlas (não dá para resolver no código)
1. **Database Access:** apague o usuário `admin`; crie `app_fatecbd` com role **readWrite** apenas no banco `fatecbd` e senha aleatória.
2. **Network Access:** remova `0.0.0.0/0`; adicione só o IP do servidor/rede.
3. **Troque a senha antiga**: `admin:root` já foi exposta no zip e no GitHub.
4. Se a senha já foi commitada, remova do histórico (`git filter-repo`) ou, mais simples, considere-a comprometida e apenas rotacione.

## Em produção
- Ative HTTPS (`server.ssl.*` ou proxy reverso). Basic Auth sem TLS expõe a senha.
- Adicione rate limiting no proxy/gateway (ex.: Nginx, Cloudflare) ou Bucket4j.
- Troque o login em memória por usuários no banco (senha com BCrypt) ou OAuth2/JWT.
- Se já existirem e-mails duplicados na coleção, remova-os antes: o índice único não será criado.
