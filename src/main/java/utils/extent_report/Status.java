package utils.extent_report;

public enum Status {

    SUCESSO("Sucesso"), FALHA("Falha"), NAO_EXECUTADO("Não executado");

    private String valor;

    Status(final String status) {
        this.valor = status;
    }

}
