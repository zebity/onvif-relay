/**
 @what Test onvif discovery_client
 
*/

package onvif_relay.test.discovery_client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.device.wsdl.Device;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.soap.SOAPFaultException;
import onvif_relay.discovery_client.jak.JakOnvifDiscoveryClient;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.invokers.CheckClockSyncAndAccess;
import onvif_relay.relay.invokers.InvokeOperation;


public class TestJakOnvifDiscoveryClient {
	
  public static void main(String [] args) {
	boolean useSEI = false;
	boolean clockAccessCheck = false;
	Object[] res = null;
	String user = "admin";
	String passwd = "admin";
	String testdev = "http://127.0.0.1:9060/onvif/device_service";
	CheckClockSyncAndAccess check = null;
	Object[] chk = null;
	
    try {
      System.out.println("Running test ...");
      
      JakOnvifDiscoveryClient onvdis = new JakOnvifDiscoveryClient();
      QName type = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");
      
      List<EndpointReference> found = onvdis.probe(type);
      
      System.out.println("Probe, got: " + found.size());
      
      if (clockAccessCheck) {
	    check = new CheckClockSyncAndAccess();
	    // Test known digest auth device
	    System.out.println("Testing diget auth on: '" + testdev + "'");
		chk = check.checkAccess(testdev, user, passwd);
		if (chk != null) {
		  System.out.println("Access check: '" + chk[0] + "'.");
		}
      }
      for (EndpointReference ref : found) {
        String addr = onvdis.getWSAddress(ref);
        String profiles = null;
        
    	System.out.println("Found: '" + ref.toString() + "'.");
    	System.out.println("Addr: '" + addr + "'.");
    	
    	
    	if (clockAccessCheck) {

  		  chk = check.checkAccess(addr, user, passwd);
  		  
    	  // Object[] chk = check.checkClockSync(addr, user, passwd, "digest");
    	  if (chk != null) {
    		System.out.println("Access Check: '" + chk[0] + "'.");
    		chk = check.checkClockSync(addr, user, passwd, (String)chk[0]);
    		if (chk != null && chk[0] != null) {
    	      System.out.println("Clock Sync Check: " + Long.toString((long)chk[0]));	
    		} else {
    		  System.out.println("Clock Sync Check: failed.");
    		}
    	  }
    	} else if (useSEI) {
    	  res = getDeviceDetailsDirect(addr, "GetDeviceInformation", "{ }");
    	  res = getDeviceDetails(addr, "GetProfiles", "{ }");
    	  if (res != null) {
    		GetProfilesResponse presp = (GetProfilesResponse)res[1];
    		for (Profile pr: presp.getProfiles()) {
    		  String nm = pr.getName(); 
    		}
    	  }
    	} else {
    	  getDeviceDetails(addr, "GetDeviceInformation", "{ }");
    	  getDeviceDetails(addr, "GetSystemDateAndTime", "{ }");
    	  getDeviceDetails(addr, "GetNetworkInterfaces", "{ }");
    	  getDeviceDetails(addr, "GetCapabilities", "{ \"category\": [\"ALL\"] }");
    	  getDeviceDetails(addr, "GetServices", "{ }");
    	  res = getDeviceDetails(addr, "GetProfiles", "{ }");
    	  if (res != null) {
    	    profiles = (String)res[0];
    	
    	    if (profiles != null) {
    	      ObjectMapper omapper = new ObjectMapper();
    	      JsonNode jres = omapper.readTree(profiles);
    	      JsonNode jprofs = jres.get("response");
    	      if (jprofs != null) {
    	        ArrayNode jarray = (ArrayNode)jprofs.get("Profiles");
    	        int i = 0;
    	        if (jarray != null) {
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
            }
    	  }
    	}
      }

	} catch (Exception x) {
      x.printStackTrace();
	}
  }
  
  static Object[] getDeviceDetails(String addr, String getReq, String reqInput) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"admin\", \"password\": \"admin\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", "digest");
    // ctrl.put("security", "ws-security");
    ctrl.put("debug", "false");
    String peekcTest = null;

    InvokeOperation onvifop = new InvokeOperation();

    try {
      JsonRequestResponse callo = JsonRequestResponse.create(call);
      // System.out.println("This should be device json details: " + callo.response);
  
      callo.response = onvifop.invoke(callo, true, ctrl);
  
      System.out.println("read in first callo response: '" + callo.response + "'."); //read first callo response
  
      res = new Object[2];
      res[1] = callo.response;
      res[0] = callo.ser();
      
      System.out.println(res[0]); //output of first response test.
  
      if (callo.response != null) {
        peekcTest = callo.response.toString();
        // System.out.println("tested the response object: '" + peekcTest + "'.");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return res;
  }
  
  static Object[] getDeviceDetailsDirect(String addr, String getReq, String reqInput) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"admin\", \"password\": \"admin\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", "digest");
    // ctrl.put("security", "ws-security");
    ctrl.put("debug", "false");
    String peekc = null, peekt = null, peekcTest = null;
    boolean altAuth = false, authFault = false;


    InvokeOperation onvifop = new InvokeOperation();

    do {
      try {

    	authFault = false;
    	
        JsonRequestResponse callo = JsonRequestResponse.create(call);
      
        Object[] proxy = onvifop.createSEI(callo, ctrl);
      
        switch (callo.reqclass) {
          case "GetDeviceInformation": {
    	    GetDeviceInformationResponse resp = new GetDeviceInformationResponse();
    	    Holder<String> man = new Holder<>();
    	    Holder<String> mod = new Holder<>();
    	    Holder<String> firm = new Holder<>();
    	    Holder<String> serial = new Holder<>();
    	    Holder<String> hard = new Holder<>();
    	    ((Device)proxy[2]).GetDeviceInformation(man, mod, firm, serial, hard);
    	    resp.setManufacturer(man.value.toString());
    	    resp.setModel(mod.value.toString());
    	    resp.setFirmwareVersion(firm.value.toString());
    	    resp.setSerialNumber(serial.value.toString());
    	    resp.setHardwareId(hard.value.toString());
    	    callo.response = resp;
    	    res = new Object[2];
    	    res[0] = callo.ser();
    	    res[1] = resp;
            }
    	    break;
          case "GetProfiles": {
      	    List<Profile> lp = ((Media)proxy[2]).GetProfiles();
      	    GetProfilesResponse resp = new GetProfilesResponse();
      	    resp.getProfiles().addAll(lp);
      	    callo.response = resp;
      	    res = new Object[2];
      	    res[0] = callo.ser();
      	    res[1] = resp;
            }
      	    break;
        }
  
        System.out.println(res[0]); //output of first response test.
  
      } catch (SOAPFaultException sex) {
        Iterator<QName> it = sex.getFault().getFaultSubcodes();
        if (it.hasNext()) {
          QName err = it.next();
        }
        if (sex.getMessage().contains("NotAuthorized")) {
          authFault = true;
          if (! altAuth) {
            altAuth = true;
            ctrl.put("security", "ws-security");
          } else {
            altAuth = false;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } while (authFault && altAuth);
    
    return res;
  }
}
