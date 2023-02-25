package onvif_relay.relay.test;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.invokers.InvokeOperation;

public class TestCxfRelay {
  public static void main(String [] args) {
	String call = "{\"target\": \"http://127.0.0.1:9080/onvif/device_service\"," +
                    "\"user\": \"admin\", \"password\": \"admin\"," +
			        "\"reqclass\": \"GetDeviceInformation\"," +
                    "\"request\": {}" +
                  "}";
	Map<String, String> ctrl = new HashMap<>();
	ctrl.put("security", "digest");
	ctrl.put("debug", "false");
	
	
	InvokeOperation onvifop = new InvokeOperation();
	
	Gson ser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	
	JsonRequestResponse jrr = JsonRequestResponse.create(call);
	
	Object got = onvifop.invoke(jrr, true, ctrl);
	
	Class savreq = null, savresp = null;
	
	if (jrr.request instanceof Class) {
	  savreq = (Class)jrr.request;
	  jrr.request = null;
	}
	
	if (jrr.response instanceof Class) {
	  savresp = (Class)jrr.response;
	  jrr.response = null;
	}
		
	String peek = ser.toJson(jrr);
    System.out.println(peek);
    
    if (savreq != null) {
      try {
        Constructor ctor = savreq.getConstructor(null);
        
        Object o = ctor.newInstance(null);
        
        peek = ser.toJson(o);
        System.out.println(peek);
        
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
