# language: pt
@ExemploREST
Funcionalidade: REST

  Contexto:
    * call read('classpath:IniciaUtils.feature')
    * url urls.exemplo
  
  Cenario: Chamar serviço
    
    Dado path '/'
    E request { parametro: 'valor' }
    Quando method get
    Entao status 200