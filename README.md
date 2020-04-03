# processor-app

Gerenciador de cadastro de clientes e transações.

# Ferramentas utilizadas

* MySQL (imagem docker: mysql:5.7)
* Docker
* testContainer

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

## Arquivo Postman

Construimos um arquivo do postman com todas as rotas construidas