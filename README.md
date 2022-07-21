#VR Tech - Avaliação Intermediária

# Mini autorizador

A VR processa todos os dias diversas transações de Vale Refeição e Vale Alimentação, entre outras.
De forma breve, as transações saem das maquininhas de cartão e chegam até uma de nossas aplicações, conhecida como *autorizador*, que realiza uma série de verificações e análises. Essas também são conhecidas como *regras de autorização*. 

Ao final do processo, o autorizador toma uma decisão, aprovando ou não a transação: 
* se aprovada, o valor da transação é debitado do saldo disponível do benefício, e informamos à maquininha que tudo ocorreu bem. 
* senão, apenas informamos o que impede a transação de ser feita e o processo se encerra.

Sua tarefa será construir um *mini-autorizador*. Este será uma aplicação Spring Boot com interface totalmente REST que permita:

 * a criação de cartões (todo cartão deverá ser criado com um saldo inicial de R$500,00)
 * a obtenção de saldo do cartão
 * a autorização de transações realizadas usando os cartões previamente criados como meio de pagamento

## Regras de autorização a serem implementadas

Uma transação pode ser autorizada se:
   * o cartão existir
   * a senha do cartão for a correta
   * o cartão possuir saldo disponível

Caso uma dessas regras não ser atendida, a transação não será autorizada.

## Demais instruções

O modelo de dados deve ser como o descrito abaixo:

* deverão existir 2 tabelas: `cartoes` e `transacoes`
* a tabela `cartoes` deverá conter as seguintes colunas:
  * `numero` (o número do cartão. Deve suportar um valor de 16 posições, e será a chave primária da tabela)
  * `senha` (a senha do cartão. Não precisa ser cifrada ou coisa assim. Pode ser texto aberto mesmo :D)
  * `saldo` (o saldo do cartão. Por se tratar de uma versão _mini_ do autorizador, armazenaremos o saldo do cartão na própria tabela de cartão)
* a tabela `transacoes` deve conter as seguintes colunas:
  * `numero_cartao` (o número do cartão utilizado na transação)
  * `valor` (o valor da transação)
  * `data_hora` (a data/hora que ocorreu a transação)
* além disso, a coluna `numero_cartao` na tabela `transacoes` deverá ser a chave estrangeira na relação entre as tabelas. Isso permitirá que um cartão possa ter N transações

Também, na avaliação da sua solução, serão realizados os seguintes testes, nesta ordem:

 * criação de um cartão
 * verificação do saldo do cartão recém-criado
 * realização de diversas transações, verificando-se o saldo em seguida, até que o sistema retorne informação de saldo insuficiente
 * realização de uma transação com senha inválida
 * realização de uma transação com cartão inexistente
 
Para isso, é importante que os contratos abaixo sejam respeitados:

## Contratos dos serviços

### Criar novo cartão
```
Method: POST
URL: http://localhost:8080/cartoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senha": "1234"
}
```
#### Possíveis respostas:
```
Criação com sucesso:
   Status Code: 201
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
-----------------------------------------
Caso o cartão já exista:
   Status Code: 422
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
```

### Obter saldo do Cartão
```
Method: GET
URL: http://localhost:8080/cartoes/{numeroCartao} , onde {numeroCartao} é o número do cartão que se deseja consultar
```

#### Possíveis respostas:
```
Obtenção com sucesso:
   Status Code: 200
   Body: 495.15 
-----------------------------------------
Caso o cartão não exista:
   Status Code: 404 
   Sem Body
```

### Realizar uma Transação
```
Method: POST
URL: http://localhost:8080/transacoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senhaCartao": "1234",
    "valor": 10.00
}
```

#### Possíveis respostas:
```
Transação realizada com sucesso:
   Status Code: 201
   Body: OK 
-----------------------------------------
Caso alguma regra de autorização tenha barrado a mesma:
   Status Code: 422 
   Body: SALDO_INSUFICIENTE|SENHA_INVALIDA|CARTAO_INEXISTENTE (dependendo da regra que impediu a autorização)
```