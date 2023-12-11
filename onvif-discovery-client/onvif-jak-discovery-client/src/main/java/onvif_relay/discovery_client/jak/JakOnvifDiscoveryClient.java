/*
 @what test ONVIF Discovery Client
*/

package onvif_relay.discovery_client.jak;

import java.util.List;
import jakarta.xml.ws.EndpointReference;
import org.apache.cxf.ws.discovery.WSDiscoveryClient;

public class JakOnvifDiscoveryClient {
  // Use WS-Discovery to find references to services that implement the Greeter portType
  
  public JakOnvifDiscoveryClient() {
  }

  public void probe() {

    try (WSDiscoveryClient client = new WSDiscoveryClient()) {
      client.setVersion10();
      client.setDefaultProbeTimeout(1000);
      
      System.out.println("Probe on: '" + client.getAddress() + "'.");
      // or: new WSDiscoveryClient("soap.udp://proxyhost:3702");
      // List<EndpointReference> references = client.probe(new QName("http://cxf.apache.org/hello_world/discovery", "Greeter"));
      List<EndpointReference> references = client.probe();
         
      // GreeterService service = new GreeterService();
      //loop through all of them and have them greet me.
      
      System.out.println("Probe, got: " + references.size());
      for (EndpointReference ref : references) {
        // Greeter g = service.getPort(ref, Greeter.class);
        // System.out.println(g.greetMe("World"));
    	System.out.println("Ref: '" + ref.toString() + "'.");
      }
    } catch (Exception ex) {
      System.out.println(ex);
    }
  } 
}
