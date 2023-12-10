/*
 @what test ONVIF Discovery Client
*/

package onvif_relay.discovery-client.jak;

public class JakOnvifDiscoveryClient {

  public JakOnvifDiscoveryClient() {
  }


//Use WS-Discovery to find references to services that implement the Greeter portType
WSDiscoveryClient client = new WSDiscoveryClient();
   // or: new WSDiscoveryClient("soap.udp://proxyhost:3702");
List<EndpointReference> references = client.probe(new QName("http://cxf.apache.org/hello_world/discovery", "Greeter"));
client.close();
         
GreeterService service = new GreeterService();
//loop through all of them and have them greet me.
for (EndpointReference ref : references) {
    Greeter g = service.getPort(ref, Greeter.class);
    System.out.println(g.greetMe("World"));
} 

}
