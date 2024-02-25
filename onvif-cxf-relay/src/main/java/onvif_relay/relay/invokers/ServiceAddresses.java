package onvif_relay.relay.invokers;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.onvif.ver10.device.wsdl.GetCapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetServicesResponse;
import org.onvif.ver10.device.wsdl.Service;
import org.onvif.ver10.schema.DeviceCapabilities;
import org.onvif.ver10.schema.MediaCapabilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import onvif_relay.relay.converters.OnvifOperations;

public class ServiceAddresses {
  Map<String, String> srvAddrs = new HashMap<>();
  Map<String, String> ns = new HashMap<>();
  Map<String, String> cap = new HashMap<>();
  String onvifSrv = null;
  String[][] nspack = {
	{ "http://www.onvif.org/ver10/replay/wsdl", "org.onvif.ver10.replay.wsal" },
	{ "http://www.onvif.org/ver20/analytics/wsdl", "org.onvif.ver20.analytics.wsdl" },
	{ "http://www.onvif.org/ver10/recording/wsdl", "org.onvif.ver10.recording.wsdl" },
	{ "http://www.onvif.org/ver20/media/wsdl", "org.onvif.ver20.media.wsdl"},
	{ "http://www.onvif.org/ver20/ptz/wsdl", "org.onvif.ver20.ptz.wsdl"},
	{ "http://www.onvif.org/ver10/device/wsdl", "org.onvif.ver10.device.wsdl" },
	{ "http://www.onvif.org/ver10/deviceIO/wsdl", "org.onvif.ver10.deviceIO.wsdl" },
	{ "http://www.onvif.org/ver10/search/wsdl", "org.onvif.ver10.search.wsd" },
	{ "http://www.onvif.org/ver10/media/wsdl", "org.onvif.ver10.media.wsdl" },
	{ "http://www.onvif.org/ver10/events/wsdl", "org.onvif.ver10.events.wsdl" }
    };
  String[][] cappack = {
	{ "Device", "org.onvif.ver10.device.wsdl" },
	{ "Media", "org.onvif.ver10.media.wsdl" },
    };
  
  public ServiceAddresses(String addr) {
    onvifSrv = addr;
    for (String[] np: nspack) {
      ns.put(np[1], np[0]);
    }
    for (String[] nc: cappack) {
      cap.put(nc[1], nc[0]);
    }
    
  }
  
  public void LoadServices(GetServicesResponse resp) {

    if (resp != null) {
      for (Service sv: resp.getService()) {
    	srvAddrs.put(sv.getNamespace(), sv.getXAddr());
      }
    }
  }
  
  public void LoadServices(GetCapabilitiesResponse resp) {

	if (resp != null) {
	  DeviceCapabilities dcap = resp.getCapabilities().getDevice();
	  srvAddrs.put("Device", dcap.getXAddr());
	  MediaCapabilities mcap = resp.getCapabilities().getMedia();
	  srvAddrs.put("Media", dcap.getXAddr());	
	}
  }
  
  public void dump(PrintStream out) {

	try {
	  ObjectMapper ser = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerModule(new JakartaXmlBindAnnotationModule());
      ser.writeValue(out, srvAddrs);
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
  }
  
  public String getServiceAddress(String srv) {
	String res = onvifSrv;
	
	String jp = OnvifOperations.getOperationNamespace(srv, null);
	if (jp != null) {
	  String key = ns.get(jp);
	  if (key != null) {
	    String saddr = srvAddrs.get(key);
	    if (saddr != null) {
	      res = saddr;
	    } else {
	      key = cap.get(jp);
	      if (key != null) {
	        saddr = srvAddrs.get(key);
	        if (saddr != null) {
	          res = saddr;
	        }
	      }
	    }
	  }
	}
	return res;
  }
}
