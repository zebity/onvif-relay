/**
 @what Test onvif discovery_client
 
*/

package onvif_relay.test.discovery_client;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetServicesResponse;
import org.onvif.ver10.device.wsdl.Service;
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
import onvif_relay.relay.converters.OnvifOperations;
import onvif_relay.relay.invokers.CheckClockSyncAndAccess;
import onvif_relay.relay.invokers.InvokeOperation;
import onvif_relay.relay.invokers.ServiceAddresses;


public class TestJakOnvifDiscoveryClient {

  static public class ControlDiscovery {
    String user = "admin";
    String passwd = "admin";
    String auth = "digest";
    boolean useSEI = false;
    boolean checkClock = false;
    boolean checkAccess = false;
    boolean doProbe = true;
    boolean filter = false;
    Set<String> filterIP = new HashSet<>();
    Map<String, String> services = new HashMap<>();
  }
  
  public static void main(String [] args) {
	ControlDiscovery ctl = new ControlDiscovery();
	Object[] res = null;
	String testdev = "http://127.0.0.1:9060/onvif/device_service";
	Object[] chk = null;
	List<EndpointReference> found = null;
	CheckClockSyncAndAccess check = null;
	ServiceAddresses saddrs = null;
	
	ParseArgs(args, ctl);
	
    try {
      System.out.println("Running test ...");
      
      JakOnvifDiscoveryClient onvdis = new JakOnvifDiscoveryClient();
      QName type = new QName("http://www.onvif.org/ver10/network/wsdl", "NetworkVideoTransmitter");
      
      if (ctl.doProbe) {
        found = onvdis.probe(type);
        System.out.println("Probe, got: " + found.size());
      }
      
      int i = 0;
      boolean useAddr = true;
      boolean haveAddr = ctl.filterIP.size() != 0 || found != null;
      EndpointReference ref = null;
      
      // Object[] ips = ctl.filterIP.toArray();
      Iterator<String> nextIp = ctl.filterIP.iterator();
      
      while (haveAddr) {
    	
    	String addr = null;
    	
    	if (ctl.doProbe) {
    	  ref = found.get(i);
          addr = onvdis.getWSAddress(ref);
    	  URL uparse = new URL(addr);
    	  if (ctl.filter) {
    		useAddr = false;
    		String host = uparse.getHost();
    	    useAddr = ctl.filterIP.contains(host);
    	  }
    	  System.out.println("Found: '" + ref.toString() + "'.");
    	  i++;
    	  haveAddr = i < found.size();
    	} else {
    	  addr = "http://" + (String)nextIp.next() + "/onvif/device_service";
    	  System.out.println("Using: '" + addr + "'.");
    	  haveAddr = nextIp.hasNext();
    	}
    	
        saddrs = new ServiceAddresses(addr);
        String profiles = null;
        
    	System.out.println("Addr: '" + addr + "'.");
    	
    	
    	if (useAddr && ctl.checkClock) {

  		  chk = check.checkAccess(addr, ctl.user, ctl.passwd);
  		  
    	  // Object[] chk = check.checkClockSync(addr, user, passwd, "digest");
    	  if (chk != null) {
    		System.out.println("Access Check: '" + chk[0] + "'.");
    		chk = check.checkClockSync(addr, ctl.user, ctl.passwd, (String)chk[0]);
    		if (chk != null && chk[0] != null) {
    	      System.out.println("Clock Sync Check: " + Long.toString((long)chk[0]));	
    		} else {
    		  System.out.println("Clock Sync Check: failed.");
    		}
    	  }
    	}
    	
    	if (useAddr && ctl.useSEI) {
    	  res = getDeviceDetailsDirect(saddrs, "GetDeviceInformation", "{ }", ctl);
    	  res = getDeviceDetailsDirect(saddrs, "GetServices", "{ \"includeCapability\": \"true\" }", ctl);
    	  if (res != null) {
    	    saddrs.LoadServices((GetServicesResponse)res[1]);
     	  }

    	  res = getDeviceDetails(saddrs, "GetProfiles", "{ }", ctl);
    	  if (res != null) {
    		GetProfilesResponse presp = (GetProfilesResponse)res[1];
    		for (Profile pr: presp.getProfiles()) {
    		  String nm = pr.getName(); 
    		}
    	  }
    	} else if (useAddr) {
    	  res = getDeviceDetails(saddrs, "GetDeviceInformation", "{ }", ctl);
    	  res = getDeviceDetails(saddrs, "GetServices", "{ \"includeCapability\": \"true\" }", ctl);
    	  if (res != null) {
      	    saddrs.LoadServices((GetServicesResponse)res[1]);
       	  }
    	  
    	  getDeviceDetails(saddrs, "GetSystemDateAndTime", "{ }", ctl);
    	  getDeviceDetails(saddrs, "GetNetworkInterfaces", "{ }", ctl);
    	  getDeviceDetails(saddrs, "GetCapabilities", "{ \"category\": [\"ALL\"] }", ctl);
    	  res = getDeviceDetails(saddrs, "GetProfiles", "{ }", ctl);
    	  if (res != null) {
    	    profiles = (String)res[0];
    	
    	    if (profiles != null) {
    	      ObjectMapper omapper = new ObjectMapper();
    	      JsonNode jres = omapper.readTree(profiles);
    	      JsonNode jprofs = jres.get("response");
    	      if (jprofs != null) {
    	        ArrayNode jarray = (ArrayNode)jprofs.get("Profiles");
    	        int j = 0;
    	        if (jarray != null) {
                  while (true) {
                    JsonNode jprof = jarray.get(j);
            
                    if (jprof == null) {
                      break;
                    } else {
                      String nm = jprof.get("Name").textValue();
                      System.out.println("Profile Name: '" + nm + "'.");
                      String req = "{ \"profileToken\": " + "\"" + nm + "\" }";
                      getDeviceDetails(saddrs, "GetProfile", req, ctl);
                      j++;
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
  
  static void ParseArgs(String[] argv, ControlDiscovery ctl) {
    int i = 0;
    String usage = "[-u user:passwd] [-f(ilter) ip,ip,ip] [-p(robe) (y|n)] [-s(ei)] [-c(lock check)] [-a(cess check)]";
    
    if (argv.length > 0) {
    
      while (i < argv.length) {
    	  
        switch(argv[i]) {
          case "-u": i++;
                     String[] up = argv[i].split(":");
                     ctl.user = up[0];
                     ctl.passwd = up[1];
                     i++;
                     break;
          case "-f": i++;
                     String[] ips = argv[i].split(",");
                     for (String ip: ips) {
                       ctl.filterIP.add(ip);
                     }
                     ctl.filter = true;
                     i++;
                     break;
          case "-p": i++;
                     if (argv[i].equals("n")) {
                       ctl.doProbe = false;
                     }
                     i++;
                     break;
          case "-s": ctl.useSEI = true;
                     i++;
                     break;
          case "-c": ctl.checkClock = true;
                     i++;
                     break;
          case "-a": ctl.checkAccess = true;
                     i++;
                     break;
          default: System.out.println("Usage: " + usage);
                   System.exit(1);
        }  
      }
    }
  }
  
  static Object[] getDeviceDetails(ServiceAddresses saddr, String getReq, String reqInput, ControlDiscovery ctl) {
	Object[] res = null;
	
	String addr = saddr.getServiceAddress(getReq);
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"" + ctl.user + "\", \"password\": \"" + ctl.passwd + "\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", ctl.auth);
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
  
  static Object[] getDeviceDetailsDirect(ServiceAddresses saddr, String getReq, String reqInput, ControlDiscovery ctl) {
	Object[] res = null;
	
	String addr = saddr.getServiceAddress(getReq);
	String call = "{\"target\": \"" + addr + "\"," +
                "\"user\": \"" + ctl.user + "\", \"password\": \"" + ctl.passwd + "\"," +
		        "\"reqclass\": \"" + getReq + "\"," +
                "\"request\": " + reqInput +
              "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", ctl.auth);
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
          case "GetServices": {
            List<Service> ls = ((Device)proxy[2]).GetServices(true);
            GetServicesResponse resp = new GetServicesResponse();
            resp.getService().addAll(ls);
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
