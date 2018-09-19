function () {

    var env = karate.env;
    karate.log('Propriedade karate.env system recebida é:', env);
    if (env == null || env == 'null') {
        env = 'homol'; // default
        karate.log('Setando karate.env como [' + env + ']');
    }

    // Configuração do ambiente
    var config = {};

    // Muda configurações de acordo com ambiente selecionado
    if (env == 'homol') {
        config.dbConfig = {};
        config.dbConfig.url = 'jdbc:oracle:thin:@999.99.99.99:1521:coredb';
        config.dbConfig.username = 'username';
        config.dbConfig.password = 'password';
        config.dbConfig.driverClassName = 'oracle.jdbc.driver.OracleDriver';

        config.urls = {};
        config.urls.exemplo = 'http://teste.net:8001/';

    } else if (env == 'dev') {
        // Nada aqui ainda
    }

    // Timeout máximo nas chamadas em milisegundos
    karate.configure('connectTimeout', 200000);
    karate.configure('readTimeout', 200000);
    karate.configure('logPrettyRequest', true);
    karate.configure('logPrettyResponse', true);
    karate.configure('afterScenario', function () {
        karate.call('classpath:AfterScenario.feature');
    });

    return config;
}