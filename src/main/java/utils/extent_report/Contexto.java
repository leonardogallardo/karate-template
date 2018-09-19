package utils.extent_report;

import com.google.common.collect.Lists;

import java.util.List;

public class Contexto {

    private Status status = Status.SUCESSO;

    private List<Step> steps = Lists.newArrayList();

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
