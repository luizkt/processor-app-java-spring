# processor-app

Gerenciador de cadastro de clientes e transações.

# Ferramentas utilizadas

* MySQL (imagem docker: mysql:5.7)
* Docker
* testContainer (testes integrados)

O banco de dados MySQL foi utilizado pela simplicidade na utilização e por conta do modelo de dados se encaixar bem com 
as relações entre as entidades Estudantes (Student) e de Transações(Transaction).

Optamos por utilizar o docker tanto para o banco de dados como para a aplicação pela facilidade de testar/implantar em 
outros ambientes.

Para os testes integrados utilizamos a biblioteca testContainer para gerar um conteiner docker de MySQL no momento de 
realizar os testes integrados das classes de serviços(services) e de repositorios(repository). 

## Execução local via docker-compose(Aplicação + MySQL)

```sh
$ docker-compose up
```

## Execução no play-with-docker

O conteiner da aplicação foi criado com a tag `luizkt/processor-app-spring`.

Recomenda-se a utilização da ultima versão `latest`.

```sh
$ docker pull luizkt/processor-app-spring:latest
```

### Variáveis de ambiente

* SPRING_DATASOURCE_URL - URL completa do banco de dados(jdbc:mysql://${HOST}:${PORT}/${DB_NAME}).
* SPRING_DATASOURCE_USERNAME - Usuário do banco de dados.
* SPRING_DATASOURCE_PASSWORD - Senha do banco de dados

```sh
$ docker run --name processor_container \\ 
-e SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/processordb?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false \\
-e SPRING_DATASOURCE_USERNAME=processorUser \\
-e SPRING_DATASOURCE_PASSWORD=fiap2020 \\
-p 8080:8080 \\
-d luizkt/processor-app-spring:latest
```

## Documentação de rotas

Construimos um arquivo do postman com todas as rotas construidas para facilitar a utilização da API, os arquivos estão 
dentro da pasta `postman_libs`.
 
 A documentação swagger pode ser consultada na URL: `~/processor-app/api/swagger-ui.html` 