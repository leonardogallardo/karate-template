package utils.extent_report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import exceptions.InesperadoException;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JsonTransformer {

    private JsonTransformer() {
    }

    private static final String REPORTS = System.getProperty("user.dir") + File.separator + "target" + File.separator + "surefire-reports" + File.separator;

    public static List<Funcionalidade> getExecucoesComoObjeto() {
        File[] fileList = new File(REPORTS).listFiles();
        if (fileList != null) {
            return Arrays.asList(fileList).stream()
                    .filter(file -> file.getName().endsWith(".json"))
                    .map(JsonTransformer::getFuncionalidade).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private static Funcionalidade getFuncionalidade(File file) {

        Map<String, Object> mapaFuncionalidade = getMapFuncionalidade(file);
        String nome = getValue(mapaFuncionalidade, "description");
        String caminho = getValue(mapaFuncionalidade, "name");

        Funcionalidade f = new Funcionalidade(nome, caminho);

        if (mapaFuncionalidade.get("tags") != null) {
            ((List) mapaFuncionalidade.get("tags")).stream().forEach(m -> f.addTag(getValue((Map) m, "name")));
        }

        AtomicReference<Cenario> cenario = new AtomicReference<>();
        ((List) mapaFuncionalidade.get("elements")).forEach(map -> {
            Map mapaCenario = (Map) map;
            String type = getValue(mapaCenario, "type");

            if (type.equals("background")) {
                cenario.set(new Cenario());

                Contexto contexto = getContexto(cenario.get(), mapaCenario);
                cenario.get().setContexto(contexto);

                if (contexto.getSteps().stream().anyMatch(step -> step.getStatus().equals(Status.FALHA))) {
                    cenario.get().setStatus(Status.FALHA);
                }

            } else if (type.equals("scenario")) {

                if (cenario.get() == null) {
                    cenario.set(new Cenario());
                }
                cenario.get().setNome(mapaCenario.get("name").toString());

                ((List) mapaCenario.get("steps")).forEach(obj -> {
                    Step step = getStep((Map) obj);
                    cenario.get().addStep(step);

                    if (step.getStatus().equals(Status.FALHA)) {
                        cenario.get().setStatus(Status.FALHA);
                    }

                });

                f.addCenario(cenario.get());
                cenario.set(null);
            } else {
                throw new InesperadoException(
                        MessageFormat.format("Tipo não conhecido: [{0}]", type));
            }
        });
        return f;
    }

    private static Contexto getContexto(Cenario cenario, Map mapaCenario) {
        Contexto contexto = new Contexto();
        ((List) mapaCenario.get("steps")).forEach(obj -> {
            Step step = getStep((Map) obj);
            contexto.addStep(step);

            if (step.getStatus().equals(Status.FALHA)) {
                contexto.setStatus(Status.FALHA);
                cenario.setStatus(Status.FALHA);
            }
        });
        return contexto;
    }

    private static Map<String, Object> getMapFuncionalidade(File file) {

        List list;

        try {
            list = new ObjectMapper().readerFor(Object.class).readValue(file);
        } catch (IOException e) {

            try {
                Files.write(Paths.get(file.getPath()),
                        Charset.forName("UTF-8").encode(
                                Charset.forName("windows-1252").decode(
                                        ByteBuffer.wrap(Files.readAllBytes(Paths.get(file.getPath()))))).array());
                list = new ObjectMapper().readerFor(Object.class).readValue(file);
            } catch (IOException e1) {
                throw new InesperadoException(MessageFormat.format("Erro ao ler arquivo [{0}]", file.getAbsolutePath()), e);
            }
        }
        return (Map<String, Object>) list.get(0);
    }

    private static Step getStep(Map obj) {
        Map mapaStep = obj;
        Map result = (Map) mapaStep.get("result");
        List<Map<String, String>> comMap = (List<Map<String, String>>) mapaStep.get("comments");
        Map<String, String> docMap = (Map<String, String>) mapaStep.get("doc_string");

        String palavraChave = getValue(mapaStep, "keyword");
        String descricao = getValue(mapaStep, "name");
        String duracao = getValue(result, "duration");
        String status = getValue(result, "status");
        String log = getValue(result, "error_message");
        List<String> comentarios = comMap != null
                ? comMap.stream().map(map -> map.get("value")).collect(Collectors.toList())
                : Lists.newArrayList();
        String docString = docMap != null
                ? getValue(docMap, "value")
                : "";

        return new Step(palavraChave, descricao, duracao, log, status, comentarios, docString);
    }

    private static String getValue(Object map, String value) {
        Object o = ((Map) map).get(value);
        return o != null ? o.toString() : "";
    }


}
