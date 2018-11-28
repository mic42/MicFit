package de.wullenweber_informatik.micfit;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by wullmicz on 05.11.2018.
 */

public class Trainingsplan {
    public static class TrainingsEinheit {
        public String _sName = "Name";
        public String _sDescription = "Descr";
        public int _iDauer_sec = 10;
        public int _iAbschnitte = 1;
        public String _sPicture;
    }

    public ArrayList<TrainingsEinheit> _Einheiten = new ArrayList<TrainingsEinheit>();
    public int _iWarteZeitAnfang_sec = 10;
    public int _iPause_sec = 3;
    public String _sPicDirectory = ".";

    public static Trainingsplan Parse(String sXMLFile)
    {
        System.out.println("Parsing file: " + sXMLFile);

        Trainingsplan trp = new Trainingsplan();

        try
        {
            File fXmlFile = new File(sXMLFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            trp._iWarteZeitAnfang_sec = Integer.parseInt(doc.getDocumentElement().getAttribute("WarteZeitAnfang_sec"));
            trp._iPause_sec = Integer.parseInt(doc.getDocumentElement().getAttribute("Pause_sec"));
            trp._sPicDirectory = doc.getDocumentElement().getAttribute("PicDirectory");

            NodeList nList = doc.getElementsByTagName("Einheit");

            for (int temp = 0; temp < nList.getLength(); ++temp)
            {
                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Trainingsplan.TrainingsEinheit te = new Trainingsplan.TrainingsEinheit();

                    Element eElement = (Element) nNode;

                    te._sName = eElement.getAttribute("Name");
                    te._sDescription = eElement.getAttribute("Description");
                    te._iDauer_sec = Integer.parseInt(eElement.getAttribute("Dauer_sec"));
                    te._iAbschnitte = Integer.parseInt(eElement.getAttribute("Abschnitte"));
                    te._sPicture = eElement.getAttribute("Picture");

                    trp._Einheiten.add(te);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return trp;
    }
}
