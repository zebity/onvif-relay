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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
      
      // ObjectMapper jser = new ObjectMapper();
      // jser.enable(SerializationFeature.INDENT_OUTPUT);
      // jser.registerModule(new JakartaXmlBindAnnotationModule());
      
      // GetCapabilities getcap = new GetCapabilities();
      // CapabilityCategory capcat = CapabilityCategory.fromValue("All");
      // getcap.getCategory().add(capcat);
      // String peek = jser.writeValueAsString(getcap);
      // System.out.println(peek);
      
      List<EndpointReference> found = onvdis.probe(type);
      
      System.out.println("Probe, got: " + found.size());
      for (EndpointReference ref : found) {
        String addr = onvdis.getWSAddress(ref);
        String profiles = null;
        
    	System.out.println("Found: '" + ref.toString() + "'.");
    	System.out.println("Addr: '" + addr + "'.");
    	getDeviceDetails(addr, "GetDeviceInformation", "{ }");
    	getDeviceDetails(addr, "GetSystemDateAndTime", "{ }");
    	getDeviceDetails(addr, "GetNetworkInterfaces", "{ }");
    	getDeviceDetails(addr, "GetCapabilities", "{ \"category\": [\"ALL\"] }");
    	getDeviceDetails(addr, "GetServices", "{ }");
    	profiles = getDeviceDetails(addr, "GetProfiles", "{ }");
    	
    	if (profiles != null) {
    	  ObjectMapper omapper = new ObjectMapper();
    	  JsonNode jres = omapper.readTree(profiles);
    	  JsonNode jprofs = jres.get("response");
    	  ArrayNode jarray = (ArrayNode)jprofs.get("Profiles");
    	  int i = 0;
          while (true) {
            JsonNode jprof = jarray.get(i);
            
            if (jprof == null) {
              break;
            } else {
              String nm = jprof.get("Name").textValue();
              System.out.println("Profile Name: '" + nm + "'.");
              String req = "{ \"profileToken\": " + "\"" + nm + "\" }";
              getDeviceDetails(addr, "GetProfile", req);
              i++;
            }
          }
    	}
    	
    	
      }

	} catch (Exception x) {
      x.printStackTrace();
	}
  }
  
  static String getDeviceDetails(String addr, String getReq, String reqInput) {
	String res = null;
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"admin\", \"password\": \"admin\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    // ctrl.put("security", "digest");
    ctrl.put("security", "ws-security");
    ctrl.put("debug", "false");
    String peekc = null, peekt = null, peekcTest = null;

    InvokeOperation onvifop = new InvokeOperation();

    try {
      JsonRequestResponse callo = JsonRequestResponse.create(call);
      // System.out.println("This should be device json details: " + callo.response);
  
      callo.response = onvifop.invoke(callo, true, ctrl);
  
      System.out.println("read in first callo response: '" + callo.response + "'."); //read first callo response
  
      res = callo.ser();
      System.out.println(res); //output of first response test.
  
      peekcTest = callo.response.toString();
      System.out.println("tested the response object: '" + peekcTest + "'.");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return res;
  }
}
