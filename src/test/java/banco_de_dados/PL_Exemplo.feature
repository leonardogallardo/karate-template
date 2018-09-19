# language: pt
@ExemploPL
Funcionalidade: PL SQL

  Contexto:
    * call read('classpath:IniciaUtils.feature')

  Cenario: Chamar function

    * def pSaida1 = new ParametroDB('result', Types.BOOLEAN)
    * def pSaida1 = new ParametroDB('po_nqtdalinea11', Types.FLOAT)
    * def pSaida2 = new ParametroDB('po_nqtdalinea12', Types.FLOAT)

    * def pEntrada1 = new ParametroDB('pi_cconta', Types.VARCHAR, '00001-9')
    * def pEntrada2 = new ParametroDB('pi_nperiodo', Types.FLOAT, '10')

    Dado def saida = [#(pSaida1), #(pSaida2)]
    E def entrada = [#(pEntrada1), #(pEntrada2)]
    Quando def retorno = db.executaFunction('pkgl_ccr1_util', 'fncl_chequesdevolvidos', entrada, saida)
    Entao print retorno

#    Dado def sql = 'select * '
#    Quando def retorno = db.procuraLinhas(sql)
#    Entao match retorno.usuario == 'batata'
