package utils.extent_report;

import com.google.common.collect.Lists;

import java.util.List;

public class Funcionalidade {

    private final String nome;

    private final String caminho;

    private List<String> tags = Lists.newArrayList();

    private List<Cenario> cenarios = Lists.newArrayList();

    public Funcionalidade(final String nome, final String caminho) {
        this.nome = nome;
        this.caminho = caminho;
    }

    public String getNome() {
        return nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public void addTag(final String tag) {
        tags.add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

    public void addCenario(final Cenario cenario) {
        cenarios.add(cenario);
    }

    public List<Cenario> getCenarios() {
        return cenarios;
    }

}
