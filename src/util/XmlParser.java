package util;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {
	public static Vector<String> getPropertiesFiles(File f){
		
		 Vector<String> result = new Vector(0, 1);
		 try {

				
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("property");


			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);


			

					Element eElement = (Element) nNode;

					String file_attribute = eElement.getAttribute("file");
					if(file_attribute != null && file_attribute.length() >0)
						result.add(file_attribute);
					

				
			}
		    } catch (Exception e) {
		    	e.printStackTrace();
		 }

		 return result;
	}
	
}
