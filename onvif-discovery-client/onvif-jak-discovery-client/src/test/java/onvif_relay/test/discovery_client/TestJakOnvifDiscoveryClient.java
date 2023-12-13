/**
 @what Test onvif discovery_client
 
*/

package onvif_relay.test.discovery_client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.onvif.ver10.device.wsdl.GetCapabilities;
import org.onvif.ver10.schema.CapabilityCategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import jakarta.xml.ws.EndpointReference;
import onvif_relay.discovery_client.jak.JakOnvifDiscoveryClient;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.invokers.InvokeOperation;

public class TestJakOnvifDiscoveryClient {
	
  public static void main(String [] args) {
	
    try {
      System.out.println("Running test ...");
      
      JakOnvifDiscoveryClient onvdis = new JakOnvifDiscoveryClient();
      QName type = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");
      
      ObjectMapper jser = new ObjectMapper();
      jser.enable(SerializationFeature.INDENT_OUTPUT);
      jser.registerModule(new JakartaXmlBindAnnotationModule());
      
      // GetCapabilities getcap = new GetCapabilities();
      // CapabilityCategory capcat = CapabilityCategory.fromValue("All");
      // getcap.getCategory().add(capcat);
      // String peek = jser.writeValueAsString(getcap);
      // System.out.println(peek);
      
      List<EndpointReference> found = onvdis.probe(type);
      
      System.out.println("Probe, got: " + found.size());
      for (EndpointReference ref : found) {
        String addr = onvdis.getWSAddress(ref);
        
    	System.out.println("Found: '" + ref.toString() + "'.");
    	System.out.println("Addr: '" + addr + "'.");
    	getDeviceDetails(addr, "GetDeviceInformation", "{ }");
    	getDeviceDetails(addr, "GetNetworkInterfaces", "{ }");
    	getDeviceDetails(addr, "GetCapabilities", "{ \"category\": [\"ALL\"] }");
    	getDeviceDetails(addr, "GetServices", "{ }");
    	getDeviceDetails(addr, "GetProfiles", "{ }");
      }

	} catch (Exception x) {
      x.printStackTrace();
	}
  }
  
  static void getDeviceDetails(String addr, String getReq, String reqInput) {
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"admin\", \"password\": \"admin\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", "digest");
    ctrl.put("debug", "false");
    String peekc = null, peekt = null, peekcTest = null;

    InvokeOperation onvifop = new InvokeOperation();

    try {
      JsonRequestResponse callo = JsonRequestResponse.create(call);
      System.out.println("This should be device json details: " + callo.response);
  
      callo.response = onvifop.invoke(callo, true, ctrl);
  
      System.out.println("read in first callo response: '" + callo.response + "'."); //read first callo response
  
      peekc = callo.ser();
      System.out.println(peekc); //output of first response test.
  
      peekcTest = callo.response.toString();
      System.out.println("tested the response object: '" + peekcTest + "'.");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
