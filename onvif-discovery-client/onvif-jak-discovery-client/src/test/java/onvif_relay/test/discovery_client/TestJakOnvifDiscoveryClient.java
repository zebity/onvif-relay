/**
 @what Test onvif discovery_client
 
*/

package onvif_relay.test.discovery_client;

import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.EndpointReference;
import onvif_relay.discovery_client.jak.JakOnvifDiscoveryClient;

public class TestJakOnvifDiscoveryClient {
	
  public static void main(String [] args) {
	
    try {
      System.out.println("Running test ...");
      
      JakOnvifDiscoveryClient onvdis = new JakOnvifDiscoveryClient();
      QName type = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");
      
      List<EndpointReference> found = onvdis.probe(type);
      
      System.out.println("Probe, got: " + found.size());
      for (EndpointReference ref : found) {
        // Greeter g = service.getPort(ref, Greeter.class);
        // System.out.println(g.greetMe("World"));
    	System.out.println("Found: '" + ref.toString() + "'.");
      }

	} catch (Exception x) {
      x.printStackTrace();
	}
  }
}
