package utils;

import com.google.common.collect.Maps;
import modelo.ParametroDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DbUtils {

    private static final Logger logger = LoggerFactory.getLogger("com.intuit.karate");
    public static final String NLINE = "\n";
    public static final String MESSAGE = "{0}: {1}";

    private final JdbcTemplate jdbc;

    public DbUtils(Map<String, Object> config) {
        String url = (String) config.get("url");
        String username = (String) config.get("username");
        String password = (String) config.get("password");
        String driver = (String) config.get("driverClassName");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        jdbc = new JdbcTemplate(dataSource);
        logger.info("init jdbc template: {}", new StringBuilder(url).append(NLINE));
    }

    public Object lerValor(String query) {
        return jdbc.queryForObject(query, Object.class);
    }

    public Map<String, Object> lerUmaLinha(String query) {
        logger.info(query);
        Map<String, Object> map = jdbc.queryForMap(query);
        map.forEach((key, value) -> logger.info(MessageFormat.format(MESSAGE, key, value)));
        Map newMap = Maps.newHashMap(map);
        map.forEach((chave, valor) -> {
            logger.info(MessageFormat.format(MESSAGE, chave, valor));
            Object o = map.get(chave);
            if (o == null) {
                newMap.remove(chave);
                newMap.put(chave, "");
            }
        });
        return newMap;
    }

    public List<Map<String, Object>> lerNLinhas(String query) {
        logger.info(query);
        List<Map<String, Object>> list = jdbc.queryForList(query);
        IntStream.range(0, list.size()).forEach(index -> {
            logger.info(MessageFormat.format("{0} Objeto número [{1}]", NLINE, index));
            list.get(index).forEach((key, value) -> logger.info(MessageFormat.format(MESSAGE, key, value)));
        });
        return list;
    }

    public void atualizar(String sql) {
        if (logger.isInfoEnabled()) {
            logger.info(NLINE.concat(sql));
        }
        jdbc.update(sql);
    }

    public void deletar(String sql) {
        atualizar(sql);
    }

    public Map<String, Object> executaFunction(final String pkg, final String nomeFunction, List<ParametroDB> parametrosEntrada, List<ParametroDB> parametrosSaida) {
        logger.info(MessageFormat.format(
                "Executando function no banco de dados [{0}], " +
                        "parâmetros de saída [{1}] " +
                        "e parâmetros de entrada[{2}]", nomeFunction, parametrosSaida, parametrosEntrada));
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("CCOR_APP_OWNER")
                .withCatalogName(pkg)
                .withFunctionName(nomeFunction);
        return executaNoDB(parametrosSaida, parametrosEntrada, jdbcCall);
    }

    public Map<String, Object> executaProcedure(final String pkg, final String nomeFunction, List<ParametroDB> parametrosEntrada, List<ParametroDB> parametrosSaida) {
        logger.info(MessageFormat.format(
                "Executando procedure no banco de dados [{0}], " +
                        "parâmetros de saída [{1}] " +
                        "e parâmetros de entrada[{2}]", nomeFunction, parametrosSaida, parametrosEntrada));
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("CCOR_APP_OWNER")
                .withCatalogName(pkg)
                .withProcedureName(nomeFunction);
        return executaNoDB(parametrosSaida, parametrosEntrada, jdbcCall);
    }

    private Map<String, Object> executaNoDB(List<ParametroDB> parametrosSaida, List<ParametroDB> parametrosEntrada, SimpleJdbcCall jdbcCall) {

        parametrosEntrada.forEach(parametroDB -> jdbcCall.addDeclaredParameter(new SqlInOutParameter(parametroDB.getNome(), parametroDB.getTipo())));
        parametrosSaida.forEach(parametroDB -> jdbcCall.addDeclaredParameter(new SqlOutParameter(parametroDB.getNome(), parametroDB.getTipo())));

        MapSqlParameterSource map = new MapSqlParameterSource();
        parametrosEntrada.forEach(parametroDB -> map.addValue(parametroDB.getNome(), parametroDB.getValor()));

        return jdbcCall.execute(map);
    }


}