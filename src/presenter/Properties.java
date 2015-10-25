package presenter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Properties {
	String port;
	String NumOfClients;

	public Properties(String path) {

    try {

	File fXmlFile = new File(path);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
				
	doc.getDocumentElement().normalize();
	
	NodeList nList = doc.getElementsByTagName("staff");
			
		Node nNode = nList.item(0);
				
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			setPort(eElement.getElementsByTagName("port").item(0).getTextContent());
			setNumOfClients(eElement.getElementsByTagName("NumOfClients").item(0).getTextContent());
			
				}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getNumOfClients() {
		return NumOfClients;
	}

	public void setNumOfClients(String numOfClients) {
		NumOfClients = numOfClients;
	}

	


}

