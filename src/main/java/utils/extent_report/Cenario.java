package utils.extent_report;

import com.google.common.collect.Lists;

import java.util.List;

public class Cenario {

    private Contexto contexto;

    private String nome;

    private Status status = Status.SUCESSO;

    private List<Step> steps = Lists.newArrayList();

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Contexto getContexto() {
        return contexto;
    }

    public void setContexto(final Contexto contexto) {
        this.contexto = contexto;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void addStep(final Step step) {
        if (Status.FALHA.equals(step.getStatus())) {
            status = Status.FALHA;
        }
        steps.add(step);
    }


}
