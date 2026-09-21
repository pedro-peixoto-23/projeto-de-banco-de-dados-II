# Projeto I de Banco de Dados II

## Objetivo

Implementar JPA no projeto desenvolvido na disciplina de POO do período anterior.

## Como executar o programa

Basta executar o arquivo `Main.java`, localizado em: `src/main/java/ifpb/Main.java`.

## Como visualizar as tabelas geradas pelo JPA

Para o projeto, foi utilizado o banco de dados H2 em memória.

As tabelas podem ser visualizadas por meio do H2 Console, disponibilizado temporariamente enquanto a aplicação estiver em execução.

### Passo a passo

1. Execute o programa normalmente pelo `Main.java`.

2. Com a aplicação ainda em execução, acesse no navegador: http://localhost:8082

3. Preencha os dados de conexão:

    - Driver Class: `org.h2.Driver`
    - JDBC URL: `jdbc:h2:mem:jpaapp;DB_CLOSE_DELAY=-1`
    - User Name: `sa`
    - Password: deixar em branco

4. Clique em `Connect`.

Após a conexão, será possível visualizar as tabelas e os dados gerados pelo JPA/Hibernate.

> Como o H2 está sendo utilizado em memória, o banco de dados deixa de existir quando a aplicação é encerrada.

## Autor

Pedro Peixoto Viana de Oliveira