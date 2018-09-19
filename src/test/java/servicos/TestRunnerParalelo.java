package servicos;

import com.intuit.karate.cucumber.CucumberRunner;
import com.intuit.karate.cucumber.KarateStats;
import cucumber.api.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.extent_report.ExtentReportGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CucumberOptions(tags = {"~@ignore", "~@RodarSequencial"})
public class TestRunnerParalelo {

    private final static String PASTA_RELATORIO = "target" + File.separator + "surefire-reports";
    private static final Logger logger = LoggerFactory.getLogger(TestRunnerParalelo.class);


    @Test
    public void testParallel() {
        long tempoInico = System.nanoTime();
        logger.info("Iniciando testes");
        KarateStats stats = CucumberRunner.parallel(getClass(), 5, PASTA_RELATORIO);
        long tempoFim = System.nanoTime();

        logger.info("Gerando relatórios");
        ExtentReportGenerator.generate();
        geraRelatorio(getTempoDecorrido(tempoInico, tempoFim));

        Assert.assertTrue("Cenários com erro: " + stats.getFailCount(), stats.getFailCount() == 0);
    }


    private static String getTempoDecorrido(final long inicio, final long fim) {
        long diferenca = fim - inicio;
        return String.format("%d min, %d sec",
                TimeUnit.NANOSECONDS.toMinutes(diferenca),
                TimeUnit.NANOSECONDS.toSeconds(diferenca) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(diferenca)));
    }

    private static void geraRelatorio(String tempoDecorrido) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(PASTA_RELATORIO), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList<String>(jsonFiles.size());
        for (File file : jsonFiles) {
            jsonPaths.add(file.getAbsolutePath());
        }
        Configuration config = new Configuration(new File("target"), "Contas");
        config.isParallelTesting();
        config.addClassifications("Tempo de execução", tempoDecorrido);
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }

}