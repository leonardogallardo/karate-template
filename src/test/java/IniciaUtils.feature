# language: pt
@ignore
Funcionalidade: Inicia Utilitários

  Cenario:
    * def TestUtils = Java.type('utils.TesteUtils')
    * def DBUtils = Java.type('utils.DbUtils')
    * def db = new DBUtils(dbConfig)
