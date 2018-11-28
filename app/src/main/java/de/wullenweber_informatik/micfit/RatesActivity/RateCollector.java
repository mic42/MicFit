package de.wullenweber_informatik.micfit.RatesActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wullmicz on 08.11.2018.
 */

public class RateCollector {

    public String getWebPage(String urlStr) {
        //progressDialog = ProgressDialog.show(this, "", "Downloading Image from " + urlStr);
        String res = "";

        InputStream in = null;
        try {
            System.out.println("getting page");
            in = openHttpConnection(urlStr);

 /*           int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                res += current;
            }
            */

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String s = r.readLine();
            while (s != null)
            {
                res += s;
                s = r.readLine();
            }

            System.out.println("done getting page");
        }catch (IOException e1) {
            e1.printStackTrace();
        }

        return res;
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public double getCurrentRate(String sWebPageContext) throws Exception
    {
        final String s1 = "<div class=\"share-price\">";

        int iPos = 0;

        iPos = sWebPageContext.indexOf(s1);
        if (iPos < 0)
        {
            throw new Exception("Tag not found: " + s1);
        }
        sWebPageContext = sWebPageContext.substring(iPos + s1.length());

        iPos = sWebPageContext.indexOf(",");
        if (iPos < 0)
        {
            throw new Exception("Tag not found: ','");
        }
        String sVorKomma = sWebPageContext.substring(0, iPos);
        String sNachKomma = sWebPageContext.substring(iPos + 1);

        iPos = sVorKomma.length() - 1;
        while (('0' <= sVorKomma.charAt(iPos)) && (sVorKomma.charAt(iPos) <= '9'))
        {
            iPos--;
        }
        sVorKomma = sVorKomma.substring(iPos+1);

        iPos = 0;
        while (('0' <= sNachKomma.charAt(iPos)) && (sNachKomma.charAt(iPos) <= '9'))
        {
            iPos++;
        }
        sNachKomma = sNachKomma.substring(0,iPos);

        String sZahl = sVorKomma + "." + sNachKomma;

        return Double.parseDouble(sZahl);
    }

    public void testGetCurrentRate(String sFileName)
    {
        try
        {
            String sZeile;
            String sContent = "";
            FileReader filereader = new FileReader (sFileName);
            BufferedReader reader = new BufferedReader (filereader);
            while ((sZeile = reader.readLine()) != null)
            {
                sContent += sZeile;
            }
            reader.close();

            double dRate = getCurrentRate(sContent);

            System.out.println("Rate: " + dRate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error!");
        }

    }

}

