## Objetivo
    Projeto exemplo de uma api de livros

### Como executar a aplicação
    Primeiro você precisará executar o clone deste repositório. Após, siga os passos abaixo.

#### Via linha de comando

1. Instalar o Apache Maven e configurá-lo no seu PATH
2. Executar, no Terminal ou Prompt de Comando "mvn spring-boot:run"
3. Para saber que tudo ocorreu com sucesso as últimas linhas do terminal deve ser identicas a estas:

   Exposing 1 endpoint(s) beneath base path '/actuator'    
   Tomcat started on port(s): 8089 (http) with context path ''    
   libraryapi.LibraryApiApplication : Started LibraryApiApplication in XXXX seconds

4. Para parar a aplicação pressione "CTRL + C"


#### Pela sua IDE favorita
    1. Faça a importação do projeto como um projeto Maven
    2. Execute a classe: LibraryApiApplication, contida no pacote 'br.com.diego.libraryapi'

### Pré-requisitos:
    Windows, MacOS, linux

### Como visualizar a documentacao da aplicacao / Swagger
    Apos iniciar a aplicacao, abra o link no navegador: http://localhost:8089/swagger-ui/index.html

### Como visualizar a o banco de dados H2
    Apos iniciar a aplicacao, abra o link no navegador: http://localhost:8089/h2-console
    JDBC URL: jdbc:h2:mem:testdb
    User Name: sa
    Password: password

### Tecnologias
    As seguintes ferramentas foram usadas na construção do projeto:
    - Java 11
    - Junit 5
    - DataBase h2

## Autores-Colaboradores
* **Diego Rocha** - *Create project*

## Achou interessante o material?
**_Deixe uma star ⭐ no repositório e um follow no [meu perfil](https://github.com/diegomedeirosrocha), isso me incentiva a criar conteúdos para comunidade**