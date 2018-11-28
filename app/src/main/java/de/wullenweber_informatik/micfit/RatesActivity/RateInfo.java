package de.wullenweber_informatik.micfit.RatesActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by wullmicz on 08.11.2018.
 */

public class RateInfo {
    public static class RateTrend {
        public String _sName = "";
        public String _sLinkDiBa = "";
        public double _KK;
        public double _VK;
        public double _RealTimeK;

        public double _AK = 0.0;
    }

    public ArrayList<RateTrend> _RateList = new ArrayList<RateTrend>();

    public static RateInfo Parse(String sXMLFile) throws ParserConfigurationException, SAXException, IOException
    {
        System.out.println("Parsing file: " + sXMLFile);

        RateInfo ri = new RateInfo();
        File fXmlFile = new File(sXMLFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nlRateTrend = doc.getElementsByTagName("_lstRates").item(0).getChildNodes();

        for (int iRateTrend = 0; iRateTrend < nlRateTrend.getLength(); ++iRateTrend)
        {
            Node ndRateTrend = nlRateTrend.item(iRateTrend);

            if (ndRateTrend.getNodeType() == Node.ELEMENT_NODE)
            {
                RateTrend rt = new RateTrend();

                for (int iNode = 0; iNode < ndRateTrend.getChildNodes().getLength(); ++iNode)
                {
                    Node ndRateNode = ndRateTrend.getChildNodes().item(iNode);

                    String ndName = ndRateNode.getNodeName();

                    if (ndName.compareToIgnoreCase("_sName") == 0)
                    {
                        rt._sName = ndRateNode.getFirstChild().getNodeValue();
                    }

                    if (ndName.compareToIgnoreCase("_sLinkDiBa") == 0)
                    {
                        rt._sLinkDiBa = ndRateNode.getFirstChild().getNodeValue();
                    }

                    if (ndName.compareToIgnoreCase("_KK") == 0)
                    {
                        rt._KK = Double.parseDouble(ndRateNode.getFirstChild().getNodeValue());
                    }

                    if (ndName.compareToIgnoreCase("_VK") == 0)
                    {
                        rt._VK = Double.parseDouble(ndRateNode.getFirstChild().getNodeValue());
                    }

                    if (ndName.compareToIgnoreCase("_RealTimeK") == 0)
                    {
                        rt._RealTimeK = Double.parseDouble(ndRateNode.getFirstChild().getNodeValue());
                    }
                }

                ri._RateList.add(rt);
            }
        }
        return ri;
    }

}
