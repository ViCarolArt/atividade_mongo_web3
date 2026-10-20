# 🔐 Atividade WEB III: Segurança do MongoApp

**Aluna:** Vitória Caroline Gouveia de Oliveira

---

## 📌 1. Sobre o projeto

O MongoApp é um sistema de cadastro de usuários desenvolvido com Java, Spring Boot e MongoDB, com interface em HTML, CSS e JavaScript.

---

## 🛡️ 2. Problemas encontrados e correções

### 🔑 2.1. Credenciais do MongoDB expostas

**Como identifiquei:** análise do `application.properties`.

**Problema:** usuário e senha estavam escritos no código.

**Como afeta:** permite que terceiros tentem acessar o banco.

**Correção:** utilização da variável de ambiente `MONGODB_URI`.

**Resultado:** credenciais fora do código; a senha antiga precisa ser trocada.

---

### 🔒 2.2. API sem autenticação

**Como identifiquei:** análise do `UsuarioController` e da configuração de segurança.

**Problema:** os endpoints podiam permitir acesso sem login.

**Como afeta:** pessoas não autorizadas poderiam manipular usuários.

**Correção:** adição do Spring Security e autenticação HTTP Basic.

**Resultado:** acesso protegido conforme as regras configuradas.

---

### 🔐 2.3. Senha do administrador no código

**Como identifiquei:** análise da classe `SecurityConfig`.

**Problema:** senha definida diretamente no código.

**Como afeta:** pode ser descoberta por quem acessa os arquivos.

**Correção:** utilizar variável de ambiente e BCrypt para proteger a senha armazenada.

**Resultado:** correção parcial; a senha fixa ainda precisa ser removida.

---

### ✅ 2.4. Falta de validação dos dados

**Como identifiquei:** análise do model `Usuario` e do controller.

**Problema:** nome vazio e e-mail inválido poderiam ser aceitos.

**Como afeta:** permite armazenar dados incorretos.

**Correção:** utilização de `@NotBlank`, `@Email`, `@Size` e `@Valid`.

**Resultado:** validações adicionadas ao model; é necessário confirmar o uso de `@Valid` no controller.

---

### ⚠️ 2.5. Vulnerabilidade de XSS

**Como identifiquei:** análise do JavaScript que utilizava `innerHTML`.

**Problema:** conteúdo malicioso poderia ser interpretado pelo navegador.

**Como afeta:** pode permitir execução de scripts indevidos.

**Correção:** utilização de `textContent` e `createElement`.

**Resultado:** exibição dos dados como texto, reduzindo o risco de XSS.

---

### 🛡️ 2.6. Rotas e permissões desprotegidas

**Como identifiquei:** análise das regras da classe `SecurityConfig`.

**Problema:** `anyRequest().permitAll()` pode liberar rotas sem autenticação.

**Como afeta:** pessoas não autorizadas podem acessar recursos protegidos.

**Correção:** utilizar `anyRequest().authenticated()` e separar permissões ADMIN e USER.

**Resultado:** precisa ser confirmado e testado na configuração atual.

---

### ⚙️ 2.7. Erros de configuração e tratamento de falhas

**Como identifiquei:** análise do `pom.xml` e das mensagens de erro de execução.

**Problema:** dependência de validação fora de `<dependencies>`, possível tratamento inadequado de erros e configuração incorreta do Java.

**Como afeta:** o projeto pode não compilar ou retornar erros inadequados.

**Correção:** ajustar o `pom.xml`, configurar o `JAVA_HOME` e padronizar as respostas HTTP.

**Resultado:** pendente de confirmação por compilação e testes.

---

## 📝 3. Conclusão

A análise identificou sete problemas importantes de segurança e configuração. Foram propostas correções para proteger as credenciais, controlar o acesso, validar os dados e reduzir vulnerabilidades no front-end.

> **Observação:** as alterações descritas precisam ser verificadas nos arquivos e testadas. O projeto corrigido ainda não teve sua compilação e execução confirmadas.

---

## 🔗 Repositório

[![GitHub](https://img.shields.io/badge/GitHub-Repositório-181717?style=for-the-badge&logo=github)](https://github.com/ViCarolArt/atividade_mongo_web3/tree/main/mongoapp)

**Disciplina:** WEB III  
**Aluna:** Vitória Caroline Gouveia de Oliveira
