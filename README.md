# Automação de serviços com karate

## Executar testes karate via linha de comando
- `mvn clean test -DargLine="-Dkarate.env={ambiente}"`

## Relatório de execução HTML karate
Quando a classe 'TestRunner' for executada, tanto manualmente ou por linha de comando, dois relatórios HTMLs serão gerados: na pasta target/cucumber-html-reports e target/extent.html. 
Para visualizar o relatório abra o arquivo overview-features.html no browser.

## Requisitos
- Java 8 (JDK 1.8.0_112 ou acima)
- Maven
- Git
