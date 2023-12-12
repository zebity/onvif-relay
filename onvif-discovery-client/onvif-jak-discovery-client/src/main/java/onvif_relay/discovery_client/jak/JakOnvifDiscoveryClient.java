/*
 @what test ONVIF Discovery Client
*/

package onvif_relay.discovery_client.jak;

import java.util.List;
import javax.xml.namespace.QName;
import jakarta.xml.ws.EndpointReference;
import org.apache.cxf.ws.discovery.WSDiscoveryClient;

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
}
