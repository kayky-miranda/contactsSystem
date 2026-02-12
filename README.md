# Contacts System

API RESTful desenvolvida com Java + Spring Boot para gerenciamento de contatos e endereços.

Projeto criado como parte de um teste técnico para vaga de desenvolvedor e aplicação de conceitos aprendidos.

# Objetivo do projeto

Disponibilizar uma API capaz de:

- Listar todos os contatos cadastrados
- Buscar contato por ID
- Criar novos contatos
- Atualizar contatos (PUT e PATCH)
- Remover contatos
- Gerenciar múltiplos endereços por contato
- Executar em ambiente de nuvem gratuito, conforme solicitado no desafio

# Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- Banco H2 (memória para testes)
- Swagger / OpenAPI
- Maven
- Git & GitHub
- Deploy em nuvem (utilizado Render)
- Consumo de API externa REST (ViaCEP) para auto preenchimento de endereços

# Arquitetura do projeto

O sistema segue o padrão de arquitetura em camadas:

- controller → recebe requisições HTTP
- service → regras de negócio e transações
- repository → acesso ao banco com JPA
- domain → entidades do sistema
- DTO → utilizado para transportar dados da API


# Estrutura de pastas

- src/main/java/com/contactsSystem

- ├── controller
- ├── service
- ├── repository
- ├── domain
- ├── dto
- └── ContactsSystemApplication.java

# Modelo de dados

DB_Contato

- id (identificador unico)
- nome (nome do contato)
- email	(e-mail unico)
- telefone (telefone unico)
- dataNascimento (data de nascimento)
- enderecos	(lista de endereços vinculados)

DB_Endereço

- id (identificador unico)
- rua (nome da rua)
- numero (número do endereço)
- cep	(cep do endereço)
- bairro (nome do bairro)
- cidade (nome da cidade)
- uf (estado)

# Endpoints da API

- GET	/contatos   (busca contato por ID)
- POST	/contatos	(cria um novo contato)
- PUT	/contatos   (atualiza todos os dados do contato (todos os dados mencionados))
- PATCH	/contatos	(atualiza parcialmente o contato)
- DELETE /contatos	(remove o contato)

# Documentação Swagger

Após iniciar a aplicação, acesse:
http://localhost:8080/swagger-ui/index.html

No ambiente em nuvem:
https://contactssystem.onrender.com/swagger-ui/index.html#/

# Como executar o projeto localmente
Pré-requisitos

- Java 17 ou superior
- Maven instalado

### Passo a passo

- Clonar o repositório:
```git clone https://github.com/kayky-miranda/contactsSystem```

- Entrar na pasta do projeto:
```cd contactsSystem```

- Executar a aplicação:
```mvn spring-boot:run```

- Aplicação disponível em:
```http://localhost:8080```

- Ambiente na núvem: ```https://contactssystem.onrender.com/swagger-ui/index.html#/```

- Banco de dados H2
Console do banco:
```http://localhost:8080/h2-console```

### Criar contato — POST /contatos

````
{
  "nome": "",
  "email": "",
  "telefone": "",
  "dataNascimento": "0000-00-10",
  "enderecos": [
    {
      "cep": "",
      "numero": ""
    }
  ]
}
````
### Editar contato — PUT, PATCH /contatos

````
{
  "nome": "",
  "email": "",
  "telefone": "",
  "dataNascimento": "0000-00-10",
  "enderecos": [
    {
      "id": ,
      "cep": "",
      "numero": ""
    }
  ]
}
````

### Configuração:

- JDBC URL: ```jdbc:h2:mem:testdb```
- User: ```sa```
- Password: ```(vazio)```

# Boas práticas aplicadas

- Arquitetura em camadas (Controller → Service → Repository)
- Uso de DTOs para transferência de dados
- Tratamento de exceções
- Uso correto dos métodos HTTP (GET, POST, PUT, PATCH, DELETE)
- Relacionamento OneToMany / ManyToOne entre entidades
- Documentação automática com Swagger
- Versionamento com Git e histórico de commits organizado
- Deploy funcional em nuvem gratuita

Desenvolvido por
Kayky Miranda

GitHub:
https://github.com/kayky-miranda