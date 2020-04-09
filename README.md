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

* MYSQL_HOST - URL do banco de dados(default: localhost)
* MYSQL_PORT - Porta do banco de dados(default: 3306)
* MYSQL_ARGS - Argumentos para a conexão
* MYSQL_DATABASE_NAME - Nome do banco de dados
* MYSQL_APPLICATION_USER - Usuário do banco de dados
* MYSQL_APPLICATION_PASSWORD - Senha do banco de dados

```sh
$ docker run --name processor-app \
-e MYSQL_HOST=db \
-e MYSQL_ARGS="useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false" \
-e MYSQL_DATABASE_NAME=processordb \
-e MYSQL_APPLICATION_USER=processorUser \
-e MYSQL_APPLICATION_PASSWORD=fiap2020 \
-p 8080:8080 \
-d luizkt/processor-app-spring:latest
```

# Documentação de rotas

Construimos um arquivo do postman com todas as rotas construidas para facilitar a utilização da API, os arquivos estão 
dentro da pasta `postman_libs`.
 
 A documentação swagger pode ser consultada na URL: `~/processor-app/api/swagger-ui.html`
 
 # Arquivo CSV para carregar massa inicial
 
 Utilizar a rota documentada `POST /processor-app/api/loader/load_from_csv` para carregar os estudantes e transações do 
 arquivo CSV  