/*
 @what test ONVIF Discovery Client
*/

package onvif_relay.discovery_client.jak;

import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;

import jakarta.xml.ws.EndpointReference;

import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.discovery.WSDiscoveryClient;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JakOnvifDiscoveryClient {
  // Use WS-Discovery to find references to services that implement the Greeter portType
  
  public JakOnvifDiscoveryClient() {
  }

  public List<EndpointReference> probe(QName type) {
    List<EndpointReference> res = null;
    
    try (WSDiscoveryClient client = new WSDiscoveryClient()) {
      client.setVersion10();
      client.setDefaultProbeTimeout(1000);
      
      // System.out.println("Probe on: '" + client.getAddress() + "'.");
      // or: new WSDiscoveryClient("soap.udp://proxyhost:3702");
      // List<EndpointReference> references = client.probe(new QName("http://cxf.apache.org/hello_world/discovery", "Greeter"));
      // QName type = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");
      res = client.probe(type);
         
      // GreeterService service = new GreeterService();
      //loop through all of them and have them greet me.
      
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return res;
  }
  
  public String getWSAddress(EndpointReference ref) {
	String res = null;
	
    Element element = DOMUtils.createDocument().createElement("ref");
    ref.writeTo(new DOMResult(element));
    NodeList nl = element.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "Address");
    
    if (nl != null && nl.getLength() > 0) {
      res = DOMUtils.getContent((Element)nl.item(0)).trim();
    }
    return res;
  }
}
