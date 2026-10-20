# 🔐 Atividade WEB III: Segurança do MongoApp

**Aluna:** Vitória Caroline Gouveia de Oliveira  
**Disciplina:** WEB III

---

## 📌 1. Sobre o projeto

O MongoApp é uma aplicação de cadastro de usuários desenvolvida com Java, Spring Boot e MongoDB Atlas, com interface em HTML, CSS e JavaScript.

O objetivo desta análise é identificar falhas de segurança no projeto, explicar seus riscos e apresentar medidas de correção para proteger as credenciais, restringir o acesso ao banco de dados e evitar acessos não autorizados à aplicação.

---

## 🛡️ 2. Problemas de segurança identificados e medidas de correção

### 🔑 2.1. Credenciais do MongoDB expostas no código

**Como foi identificado:** análise do arquivo `application.properties`.

**Problema:** a string de conexão do MongoDB continha o nome de usuário e a senha diretamente no arquivo de configuração.

**Risco:** caso o arquivo seja publicado no GitHub ou compartilhado com terceiros, as credenciais podem ser expostas, permitindo tentativas de acesso indevido ao banco de dados.

**Correção proposta:** substituir a credencial fixa por uma variável de ambiente chamada `MONGODB_URI`, utilizada pelo Spring Boot para obter a string de conexão sem armazenar a senha diretamente no código-fonte.

**Exemplo de configuração:**

```properties
spring.data.mongodb.uri=${MONGODB_URI}
```

**Medida adicional:** trocar a senha antiga no MongoDB Atlas, pois ela já esteve presente no arquivo do projeto. Também é necessário verificar se a credencial foi publicada no GitHub e impedir que arquivos contendo senhas sejam enviados ao repositório.

**Resultado esperado:** reduzir o risco de vazamento de credenciais e proteger o acesso ao banco de dados.

### 🔒 2.2. Usuário do banco de dados com permissões excessivas

**Problema:** utilizar uma conta administrativa ou um usuário com permissões superiores às necessárias aumenta o impacto de um eventual vazamento da senha.

**Risco:** um invasor que obtenha as credenciais poderá realizar operações além das necessárias para o funcionamento da aplicação.

**Correção proposta:** criar um usuário exclusivo para o MongoApp no MongoDB Atlas, com senha forte e exclusiva, concedendo apenas as permissões necessárias sobre o banco utilizado pela aplicação.

A lista de endereços IP autorizados também deve ser restrita, evitando permitir conexões de qualquer origem sem necessidade.

**Resultado esperado:** limitar os danos em caso de comprometimento das credenciais.

### 🔐 2.3. Senha do administrador da aplicação definida no código

**Como foi identificado:** análise da classe `SecurityConfig`.

**Problema:** definir uma senha fixa diretamente no código-fonte pode expor a credencial a qualquer pessoa que tenha acesso aos arquivos do projeto.

**Risco:** terceiros podem descobrir a senha e utilizá-la para acessar as funcionalidades protegidas da aplicação.

**Correção proposta:** armazenar a senha em uma variável de ambiente, evitando valores fixos no código. Quando a aplicação utiliza autenticação com senha armazenada, deve-se empregar um algoritmo apropriado de hash, como BCrypt, em vez de guardar senhas em texto puro.

É importante destacar que BCrypt protege a senha armazenada por meio de hash; ele não substitui a necessidade de manter a senha original em segredo nem significa que a senha fixa já foi removida.

**Resultado esperado:** reduzir a exposição de credenciais administrativas e melhorar a proteção das senhas utilizadas na autenticação.

### 🚪 2.4. API sem proteção adequada de acesso

**Problema:** endpoints sem autenticação ou com regras permissivas podem permitir que pessoas não autorizadas consultem, cadastrem, alterem ou excluam usuários.

**Risco:** acesso indevido aos dados e execução de operações não autorizadas.

**Correção proposta:** utilizar Spring Security para exigir autenticação nas rotas protegidas e configurar regras de autorização conforme as funções dos usuários. A autenticação HTTP Basic pode ser utilizada em um ambiente de estudo, desde que acompanhada de HTTPS quando houver comunicação pela rede.

**Resultado esperado:** impedir o acesso anônimo às funcionalidades que exigem autenticação, desde que as regras estejam corretamente configuradas e testadas.

### ✅ 2.5. Ausência de validação adequada dos dados

**Problema:** campos como nome e e-mail podem receber informações vazias ou em formatos inválidos quando não há validação no servidor.

**Risco:** armazenamento de dados inconsistentes e aumento da possibilidade de erros no processamento das requisições.

**Correção proposta:** utilizar as anotações `@NotBlank`, `@Email` e `@Size` no modelo `Usuario`, além de `@Valid` nos métodos do controller que recebem os dados.

**Resultado esperado:** rejeitar entradas inválidas antes de processá-las, desde que a validação esteja ativada corretamente no controller.

### ⚠️ 2.6. Risco de Cross-Site Scripting (XSS)

**Problema:** a utilização de `innerHTML` para exibir conteúdo recebido de usuários pode permitir que textos maliciosos sejam interpretados como HTML.

**Risco:** execução de scripts indevidos no navegador, dependendo do conteúdo inserido e do contexto de exibição.

**Correção proposta:** utilizar `textContent` para apresentar texto e `createElement` para construir elementos da interface sem interpretar dados recebidos como HTML executável.

**Resultado esperado:** reduzir o risco de XSS nas partes da interface corrigidas.

### ⚙️ 2.7. Configuração e tratamento de erros

**Problema:** configurações incorretas de dependências, versões do Java ou tratamento de exceções podem comprometer a execução e dificultar a identificação segura de falhas.

**Risco:** falhas de compilação, respostas HTTP inadequadas e possível exposição de informações internas.

**Correção proposta:** manter as dependências dentro da seção `<dependencies>` do `pom.xml`, verificar a compatibilidade do Java e padronizar as respostas de erro. Mensagens retornadas ao cliente não devem revelar senhas, strings de conexão, stack traces ou detalhes internos do banco.

**Resultado esperado:** melhorar a estabilidade da aplicação e evitar a divulgação desnecessária de informações técnicas.

---

## 📋 3. Prioridade das medidas de segurança

As medidas mais importantes para este projeto são:

1. 🔑 Trocar a senha antiga do MongoDB Atlas, pois ela estava presente no arquivo de configuração.
2. 🛡️ Criar um usuário exclusivo para o MongoApp, com senha forte e permissões mínimas.
3. 🔒 Remover as credenciais do código-fonte, utilizando a variável de ambiente `MONGODB_URI`.
4. 🔐 Retirar qualquer senha administrativa fixa do código, utilizando uma configuração segura para a autenticação.
5. 🚪 Verificar as regras de autenticação e autorização da aplicação.
6. ✅ Validar as entradas, reduzir o risco de XSS e evitar a exposição de informações em mensagens de erro.
7. 🧪 Executar os testes e verificar se as alterações realmente funcionam.

---

## 📝 4. Conclusão

A análise do MongoApp identificou riscos relacionados principalmente à exposição de credenciais do MongoDB, ao armazenamento inadequado de senhas e à possibilidade de acessos não autorizados.

A principal medida de segurança é substituir a credencial exposta por uma nova senha, criar um usuário específico para a aplicação e retirar as informações sensíveis do código-fonte. A utilização de variáveis de ambiente, permissões mínimas no MongoDB Atlas e regras adequadas de autenticação contribui para reduzir os riscos de acesso indevido.

Também são importantes a validação dos dados recebidos, a prevenção de XSS e o tratamento seguro de erros.

**⚠️ Situação das correções:** as medidas propostas devem ser verificadas nos arquivos atualizados e submetidas a testes antes de serem consideradas concluídas. A compilação bem-sucedida, isoladamente, não comprova que todas as falhas de segurança foram resolvidas.

---

## 🔗 5. Repositório

**GitHub:** inserir o link do projeto.

---
