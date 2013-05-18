/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.cnp;

import br.com.mvbos.IOWorkUtil.IOObject;
import br.com.mvbos.cnp.model.CadastroURL;
import br.com.mvbos.cnp.model.IUpadate;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbecker
 */
public class Application {

    private static final IOObject ioo = new IOObject();
    public static final File arquivo = new File("cnp.ser");
    public static int timeToCheck = 25 * 1000 * 60;
    public static boolean autoCheck = true;
    public static long nextCheck;
    public static IUpadate update;
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy mm:ss");


    public static void save(List<CadastroURL> values) {
        ioo.save(arquivo, values);
    }

    public static List<CadastroURL> load() {
        List<CadastroURL> lst = Collections.EMPTY_LIST;
        Object o = ioo.load(arquivo);

        if (o != null) {
            lst = (List<CadastroURL>) o;
        }

        return lst;
    }

    public static synchronized void startAutoCheck(final List<CadastroURL> lst) {
        new Thread() {
            @Override
            public void run() {
                while (autoCheck) {
                    if (Application.nextCheck - System.currentTimeMillis() <= 0) {
                        System.out.println("Start check: " + dateFormat.format(new Date()));
                        for (CadastroURL c : lst) {
                            verificar(c);
                        }
                        System.out.println("End check: " + dateFormat.format(new Date()));

                        if (update != null) {
                            update.update();
                        }

                        nextCheck = System.currentTimeMillis() + timeToCheck;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    public static String decodeURL(String url, int n) {
        String nURL;

        nURL = url.replaceAll("@n", String.valueOf(n));

        Calendar dt = Calendar.getInstance();
        nURL = nURL.replaceAll("@a", String.valueOf(dt.get(Calendar.YEAR)));
        String mes = dt.get(Calendar.MONTH) + 1 < 10 ? "0" + (dt.get(Calendar.MONTH) + 1) : String.valueOf(dt.get(Calendar.MONTH) + 1);
        nURL = nURL.replaceAll("@m", mes);
        nURL = nURL.replaceAll("@d", String.valueOf(dt.get(Calendar.DAY_OF_MONTH)));

        return nURL;
    }

    public static boolean validar(CadastroURL c) {
        try {
            //String regex = "\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
            String url = decodeURL(c.getUrl(), c.getContadorAtual());

            if (url.substring(url.indexOf(".") + 1).trim().length() == 0) {
                return false;
            }

            URL u = new URL(url);
            return true;

        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

        return false;
    }

    public static synchronized void verificar(CadastroURL c) {

        String nURL = null;
        try {
            nURL = decodeURL(c.getUrl(), c.getContadorProximo());

            URL u = new URL(nURL);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD"); 
            huc.connect();
            int code = huc.getResponseCode();

            if (code == 404) {
                c.setNovo(false);
                c.setStatus(Strings.SEM_MUDANCA);
            } else if (code == 200) {
                c.setNovo(true);
                c.setStatus(Strings.NOVA_PAGINA);
            }

            System.out.println("Check: " + nURL);

        } catch (Exception e) {
            c.setStatus("Erro ao ler URL: " + nURL);
        }
    }

    public static void abrirURL(String nURL) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(nURL));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void proximoLink(CadastroURL c) {
        c.setContadorAtual(c.getContadorProximo());
        c.setContadorProximo(c.getContadorProximo() + 1);
        c.setStatus(Strings.ACABOU_DE_MUDAR);
    }

    public static int getMinutosVerificacao() {
        return (timeToCheck / 1000) / 60;
    }

    public static void setMinutosVerificacao(int minutos) {
        timeToCheck = (minutos * 1000) * 60;
        nextCheck = System.currentTimeMillis() + timeToCheck;
    }
}
