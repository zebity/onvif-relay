/*
 @what - Test Xerces xml parser
*/


package test_xerces;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

public class TestXerces {
  public static void main(String[] args){

    try {
      // File inputFile = new File("input.txt");
      URL url = new URL(args[0]);
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(url.openStream());
    } catch (Exception ex) {
     System.out.println(ex);
    }
  }
} 
