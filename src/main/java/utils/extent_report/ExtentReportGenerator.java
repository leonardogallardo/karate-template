package utils.extent_report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import exceptions.InesperadoException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

public class ExtentReportGenerator {

    private ExtentReportGenerator() {
    }

    private static final String HTML_PATH = System.getProperty("user.dir")
            + File.separator + "target"
            + File.separator + "extent.html";

    private static final String CONFIG_PATH = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "extent-config.xml";

    public static void generate() {
        List<Funcionalidade> funcionalidades = JsonTransformer.getExecucoesComoObjeto();
        ExtentReports extent = getExtentReport();

        addExecutionToReport(funcionalidades, extent);

        extent.flush();
    }

    private static void addExecutionToReport(List<Funcionalidade> funcionalidades, ExtentReports extent) {
        funcionalidades.forEach(funcionalidade ->
                funcionalidade.getCenarios().stream().forEach(cenario -> {
                    ExtentTest featureReport = extent.createTest(Feature.class, cenario.getNome());

                    featureReport.assignAuthor(funcionalidade.getCaminho());
                    funcionalidade.getTags().forEach(featureReport::assignCategory);

                    ExtentTest cenarioReport = featureReport.createNode(Scenario.class, funcionalidade.getNome() + " - " + cenario.getNome());

                    cenario.getContexto().getSteps().stream().forEach(step -> createStep(cenarioReport, step));
                    cenario.getSteps().stream().forEach(step -> createStep(cenarioReport, step));

                    switch (cenario.getStatus()) {
                        case SUCESSO:
                            cenarioReport.pass(cenario.getStatus().toString());
                            break;
                        case FALHA:
                            cenarioReport.fail(cenario.getStatus().toString());
                            break;
                        case NAO_EXECUTADO:
                            cenarioReport.skip(cenario.getStatus().toString());
                            break;
                    }
                })
        );
    }

    private static void createStep(ExtentTest cenarioReport, Step step) {
        ExtentTest stepReport = cenarioReport.createNode(getGherkinKeyword(step.getPalavraChave()), step.getDescricao());
        String log = String.valueOf(
                step.getComentarios().stream().collect(Collectors.joining("\n"))
                        + "\n" + step.getDocString()
                        + "\n" + step.getLog())
                .replace("<", "&lt").replace(">", "&gt");
        switch (step.getStatus()) {
            case SUCESSO:
                stepReport.pass(log);
                break;
            case FALHA:
                stepReport.fail(log);
                break;
            case NAO_EXECUTADO:
                stepReport.skip(log);
                break;
        }
    }

    private static ExtentReports getExtentReport() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(HTML_PATH);
        htmlReporter.setAppendExisting(false);
        htmlReporter.loadXMLConfig(new File(CONFIG_PATH));

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        try {
            extent.setGherkinDialect("pt");
        } catch (UnsupportedEncodingException e) {
            throw new InesperadoException(e);
        }

        return extent;
    }

    private static GherkinKeyword getGherkinKeyword(final String palavraChave) {
        try {
            return new GherkinKeyword(palavraChave);
        } catch (ClassNotFoundException e) {
            throw new InesperadoException(e);
        }
    }


}