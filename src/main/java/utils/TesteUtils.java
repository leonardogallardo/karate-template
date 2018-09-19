package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TesteUtils {

    private static final Logger logger = LoggerFactory.getLogger("com.intuit.karate");

    public TesteUtils() {
        logger.info(System.getProperty("user.name"));
    }

    public void esperaMiliSegundos(int mili) {
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public String converteDataParaFormato(final String dataDN, final String formato) {
        return new SimpleDateFormat(formato).format(converteDataDN(dataDN));
    }

    private Date converteDataDN(String data) {
        String segundoCaracter = data.substring(1, 2);

        Calendar calendar = Calendar.getInstance();
        if (segundoCaracter.equals("-")) {
            String valor = data.substring(2, data.length());

            int dias = Integer.parseInt(valor);
            dias = dias - (dias * 2);

            calendar.add(Calendar.DAY_OF_MONTH, dias);

        } else {
            String valor = data.substring(1, data.length());

            int dias = Integer.parseInt(valor);
            calendar.add(Calendar.DAY_OF_MONTH, dias);
        }

        return calendar.getTime();
    }

}
