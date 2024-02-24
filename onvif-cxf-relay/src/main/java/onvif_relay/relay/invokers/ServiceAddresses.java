package onvif_relay.relay.invokers;

import java.util.HashMap;
import java.util.Map;

import org.onvif.ver10.device.wsdl.GetServicesResponse;
import org.onvif.ver10.device.wsdl.Service;

import onvif_relay.relay.converters.OnvifOperations;

public class ServiceAddresses {
  Map<String, String> srvAddrs = new HashMap<>();
  String onvifSrv = null;
  
  public ServiceAddresses(String addr) {
    onvifSrv = addr; 
  }
  public void LoadServices(GetServicesResponse resp) {

    if (resp != null) {
      for (Service sv: resp.getService()) {
    	srvAddrs.put(sv.getNamespace(), sv.getXAddr());
      }
    }
  }
  
  public String getServiceAddress(String srv) {
	String res = onvifSrv;
	
	String ns = OnvifOperations.getOperationNamespace(srv, null);
	if (ns != null) {
	  String saddr = srvAddrs.get(ns);
	  if (saddr != null) {
	    res = saddr;
	  }
	}
	return res;
  }
}
