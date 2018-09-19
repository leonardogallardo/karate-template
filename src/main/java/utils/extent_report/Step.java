package utils.extent_report;

import java.util.List;

public class Step {

    private final String palavraChave;

    private final String descricao;

    private final String log;

    private final String duracao;

    private final String docString;

    private final List<String> comentarios;

    private Status status = Status.NAO_EXECUTADO;

    public Step(final String palavraChave, final String descricao, final String duracao, final String log, final String status, List<String> comentarios, String docString) {
        this.palavraChave = palavraChave;
        this.descricao = descricao;
        this.duracao = duracao;
        this.log = log;

        if (status.equals("passed")) {
            this.status = Status.SUCESSO;
        } else if (status.equals("failed")) {
            this.status = Status.FALHA;
        }

        this.comentarios = comentarios;
        this.docString = docString;
    }

    public String getPalavraChave() {
        return palavraChave;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getLog() {
        return log;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getComentarios() {
        return comentarios;
    }

    public String getDocString() {
        return docString;
    }

}
