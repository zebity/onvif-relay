/**
 @what Test onvif discovery_client
 
*/

package onvif_relay.test.discovery_client;

import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;

import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetServicesResponse;
import org.onvif.ver10.device.wsdl.Service;
import org.onvif.ver10.media.wsdl.GetProfileResponse;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.media.wsdl.GetStreamUriResponse;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.CapabilityCategory;
import org.onvif.ver10.schema.MediaUri;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.StreamType;
import org.onvif.ver10.schema.Transport;
import org.onvif.ver10.schema.TransportProtocol;
import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.GetCapabilitiesResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.soap.SOAPFaultException;
import onvif_relay.discovery_client.jak.JakOnvifDiscoveryClient;
import onvif_relay.relay.converters.JsonRequestResponse;
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
    boolean dumpServices = false;
    boolean loadServices = true;
    boolean loadCapabilities = true;
    boolean ssl = false;
    boolean sslvalidate = true;
    boolean print = true;
    Set<String> filterIP = new HashSet<>();
    Map<String, String> services = new HashMap<>();
  }
  
  public static void main(String [] args) {
	ControlDiscovery ctl = new ControlDiscovery();
	Object[] res = null;
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
    	  if (ctl.ssl)
    	    addr = "https://" + (String)nextIp.next() + "/onvif/device_service";
    	    if (ctl.sslvalidate == false) {
      	      // System.setProperty("com.sun.net.ssl.checkRevocation", "false");
      	      // System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
    	      /* TrustManager trm = new X509TrustManager() {
  	            public X509Certificate[] getAcceptedIssuers() {
  	              return null;
  	            }
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					
				}
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
    	      };
    	      SSLContext sc = SSLContext.getInstance("SSL");
    	      sc.init(null, new TrustManager[] { trm }, null);
    	      javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(
    	        sc.getSocketFactory()); */

    	      /* javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
    	        new javax.net.ssl.HostnameVerifier() {
    	        	  public boolean verify(String hostname, javax.net.ssl.SSLSession ss) {
    	        	    return true;
    	        	  }
    	        }); */
    	      // System.setProperty(addr, "false")
    	    }
    	  else
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
    	
    	if (useAddr) {
    	  res = getDetails(saddrs, "GetDeviceInformation", "{ }", ctl, false);
    	  res = getDetails(saddrs, "GetServices", "{ \"includeCapability\": \"true\" }", ctl, false);
    	  if (res != null && ctl.loadServices) {
      	    saddrs.LoadServices((GetServicesResponse)res[1]);
       	  }
    	  res = getDetails(saddrs, "GetCapabilities", "{ \"category\": [\"ALL\"] }", ctl, false);
    	  if (res != null && ctl.loadCapabilities) {
        	saddrs.LoadServices((GetCapabilitiesResponse)res[1]);
      	  }
    	  
    	  if (ctl.dumpServices) {
    		saddrs.dump(System.out); 
    	  }
    	  
    	  res = getDetails(saddrs, "GetSystemDateAndTime", "{ }", ctl, true);
    	  res = getDetails(saddrs, "GetNetworkInterfaces", "{ }", ctl, true);
    	  res = getDetails(saddrs, "GetProfiles", "{ }", ctl, false);
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
                      String req = "{ \"profileToken\": \"" + nm + "\" }";
                      res = getDetails(saddrs, "GetProfile", req, ctl, false);
                      String setup = "\"streamSetup\": { \"stream\": \"RTP___UNICAST\", \"transport\": { \"protocol\": \"RTSP\" } }";
                      String strmreq = "{ " + setup + ", \"profileToken\": \"" + nm + "\" }";
                      // testSer();
                      res = getDetails(saddrs, "GetStreamUri", strmreq, ctl, false);
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
  
  static void testSer() {
  	StreamSetup ss = new StreamSetup();
  	Transport t = new Transport();
  	t.setProtocol(TransportProtocol.RTSP);
  	ss.setStream(StreamType.RTP___UNICAST);
  	ss.setTransport(t);

  	ObjectMapper ser = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerModule(new JakartaXmlBindAnnotationModule());
    
  	try {
      String val = ser.writeValueAsString(ss);
      System.out.println(val);
  	} catch (Exception ex) {
  	  ex.printStackTrace();
  	}
  }
  
  static void ParseArgs(String[] argv, ControlDiscovery ctl) {
    int i = 0;
    String usage = "[-l(oad disable) (s|c)] [-u user:passwd] [-f(ilter) ip,ip,ip] [-p(robe) (y|n)] [-t(ls/ssl) (v|d)] [-s(ei)] [-c(lock check)] [-a(cess check)] [-d(dump) (s|c)]";
    
    if (argv.length > 0) {
    
      while (i < argv.length) {
    	  
        switch(argv[i]) {
          case "-a": ctl.checkAccess = true;
                     i++;
                     break;
          case "-c": ctl.checkClock = true;
                     i++;
                     break;
          case "-d": i++;
                     if (argv[i].equals("s")) {
                       ctl.dumpServices = true;
                     }
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
          case "-l": i++;
                     if (argv[i].equals("s")) {
                       ctl.loadServices = false;
                     } else if (argv[i].equals("c")) {
                       ctl.loadCapabilities = false;
                     }
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
          case "-t": i++;
        	         ctl.ssl = true;
        	         if (argv[i].equals("v")) {
        	           ctl.sslvalidate = true;
        	         } else if (argv[i].equals("d")) {
        	           ctl.sslvalidate = false;
        	         }
                     i++;
                     break;
          case "-u": i++;
                     String[] up = argv[i].split(":");
                     ctl.user = up[0];
                     ctl.passwd = up[1];
                     i++;
                     break;
          default: System.out.println("Usage: " + usage);
                   System.exit(1);
        }  
      }
    }
  }

  static void printResult(Object[] res) {
    System.out.println("Begin Result:");
    if (res != null && res[0] != null) {
      System.out.println(res[0]);
    }
    System.out.println("End Result.");
  }
  
  static Object[] getDetails(ServiceAddresses saddr, String getReq, String reqInput, ControlDiscovery ctl, boolean override) {
	Object[] res = null;
	
	if (ctl.useSEI && ! override) {
	  res = getDeviceDetailsDirect(saddr, getReq, reqInput, ctl);
	} else {
	  res = getDeviceDetails(saddr, getReq, reqInput, ctl);
	}
	
	if (ctl.print) {
      printResult(res);
	}
	return res;
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
  
      // System.out.println("read in first callo response: '" + callo.response + "'."); //read first callo response
  
      res = new Object[2];
      res[1] = callo.response;
      res[0] = callo.ser();
      
      // System.out.println(res[0]); //output of first response test.
  
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
          case "GetProfile": {
	      	  ObjectMapper omapper = new ObjectMapper();
	      	  JsonNode jres = omapper.readTree(reqInput);
	      	  String prof = jres.get("profileToken").textValue();
      	      Profile p = ((Media)proxy[2]).GetProfile(prof);
      	      GetProfileResponse resp = new GetProfileResponse();
      	      resp.setProfile(p);
      	      callo.response = resp;
      	      res = new Object[2];
      	      res[0] = callo.ser();
      	      res[1] = resp;
            }
      	    break;
          case "GetStreamUri": {
	      	  ObjectMapper omapper = new ObjectMapper();
	      	  JsonNode jres = omapper.readTree(reqInput);
	      	  StreamSetup ss = new StreamSetup();
	      	  Transport t = new Transport();
	      	  t.setProtocol(TransportProtocol.RTSP);
	      	  ss.setStream(StreamType.RTP___UNICAST);
	      	  ss.setTransport(t);
	      	  String prof = jres.get("profileToken").textValue();
      	      MediaUri mu = ((Media)proxy[2]).GetStreamUri(ss, prof);
      	      GetStreamUriResponse resp = new GetStreamUriResponse();
      	      resp.setMediaUri(mu);
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
          case "GetCapabilities": {
        	  List<CapabilityCategory> lc = new LinkedList<>();
        	  lc.add(CapabilityCategory.ALL);
              Capabilities c = ((Device)proxy[2]).GetCapabilities(lc);
              GetCapabilitiesResponse resp = new GetCapabilitiesResponse();
              resp.setCapabilities(c);
              callo.response = resp;
              res = new Object[2];
              res[0] = callo.ser();
              res[1] = resp;
            }
            break;
        }
  
        // System.out.println(res[0]); //output of first response test.
  
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
