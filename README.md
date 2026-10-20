MongoApp 🍃

Aplicação backend desenvolvida em Java com Spring Boot, utilizando MongoDB Atlas como banco de dados NoSQL. O projeto reúne conceitos de desenvolvimento de APIs REST, segurança, validação de dados e persistência de informações.
🚀 Tecnologias utilizadas
Java 17
Spring Boot 3.5.6
Spring Web
Spring Security
Spring Validation
Spring Data MongoDB
MongoDB Atlas
Maven
📋 Pré-requisitos
Antes de executar o projeto, você precisa ter:
Java JDK 17 instalado.
MongoDB Atlas com um cluster configurado.
Um usuário de banco de dados com as permissões necessárias.
Git e Visual Studio Code (opcionais, mas recomendados).
⚙️ Configuração do ambiente
1. Clone o repositório
git clone https://github.com/SEU-USUARIO/mongoapp.git
cd mongoapp

Substitua SEU-USUARIO pelo seu nome de usuário do GitHub e ajuste o endereço caso o nome do repositório seja diferente.
2. Configure o MongoDB Atlas
No MongoDB Atlas:
Crie ou selecione um cluster.
Configure um usuário em Database Access.
Autorize o endereço IP da sua conexão em Network Access.
Obtenha a URI de conexão para sua aplicação.
3. Configure as variáveis de ambiente
O projeto utiliza variáveis de ambiente para manter as credenciais fora do código-fonte.
No terminal PowerShell do Windows, execute:
$env:MONGODB_URI = "SUA_URI_DO_MONGODB_ATLAS"
$env:APP_ADMIN_PASSWORD = "SUA_SENHA_ADMIN"
$env:APP_USER_PASSWORD = "SUA_SENHA_USUARIO"

Substitua os valores de exemplo pelos seus próprios dados. Não publique essas informações no GitHub.
Verifique também o banco de dados indicado na URI e confirme se o usuário do Atlas possui as permissões necessárias.
▶️ Como executar o projeto
Executar os testes
No terminal, dentro da pasta do projeto:
.\mvnw.cmd test

Iniciar a aplicação
.\mvnw.cmd spring-boot:run

Após a inicialização bem-sucedida, acesse:
http://localhost:8080
A disponibilidade dos endpoints e a conexão com o MongoDB dependem da configuração correta do ambiente.
🔐 Segurança
O projeto utiliza Spring Security e variáveis de ambiente para configuração de credenciais.
Boas práticas adotadas e recomendadas:
Não armazenar senhas ou credenciais do MongoDB no repositório.
Não publicar arquivos .env com dados reais.
Manter as permissões do banco limitadas ao necessário.
Autorizar apenas os endereços IP necessários no MongoDB Atlas.
Utilizar senhas fortes e diferentes para cada finalidade.
📁 Estrutura do projeto
mongoapp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml

A estrutura acima é ilustrativa. Os pacotes e as classes específicas podem variar conforme a organização do projeto.
🧪 Testes
Os testes automatizados podem ser executados com o Maven Wrapper:
.\mvnw.cmd test

O resultado esperado, quando todos os testes passam, é BUILD SUCCESS.
🎯 Objetivos do projeto
Praticar desenvolvimento backend com Java e Spring Boot.
Trabalhar com persistência de dados em MongoDB.
Implementar conceitos de segurança e validação.
Utilizar variáveis de ambiente para configuração.
Aplicar testes automatizados e boas práticas de organização de código.
👩‍💻 Autoria
Desenvolvido como projeto de estudo e prática em desenvolvimento backend.
Tecnologias principais: Java · Spring Boot · MongoDB Atlas

⭐ Se este projeto for útil para seus estudos, considere deixar uma estrela no repositório!
