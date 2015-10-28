package presenter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


/**
 * <h2>Properties</h2>
 * This Class the information of a properties file, that will load when we run our application
 * @param String port - holds the port number
 * @param String NumOfClients - holds the num of clien to be handled in the threadpool.
 * 
 * @author Alon Tal & Omry Dabush
 *
 */
public class Properties {
	String port;
	String NumOfClients;

/**
 * <h2>Constructor</2>
 * loads the properties file	
 * @param path
 */

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
/**
 * returns the port number
 * @return
 */
	public String getPort() {
		return port;
	}
	
/**
 * sets the port number
 * 
 * @param port
 */

	public void setPort(String port) {
		this.port = port;
	}
/**
 * returns the number of client limit in the threadpool
 *  
 * @return
 */

	public String getNumOfClients() {
		return NumOfClients;
	}

/**
 * sets the the number of client limit in the threadpool
 * 
 * @param numOfClients
 */
	public void setNumOfClients(String numOfClients) {
		NumOfClients = numOfClients;
	}

	


}

