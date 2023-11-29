package onvif_relay.relay.test;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.invokers.InvokeOperation;

public class TestCxfRelay {
  public static void main(String [] args) {
	String call = "{\"target\": \"http://127.0.0.1:9080/onvif/device_service\"," +
                    "\"user\": \"admin\", \"password\": \"admin\"," +
			        "\"reqclass\": \"GetDeviceInformation\"," +
                    "\"request\": {}" +
                  "}";
	String test = "{\"target\": \"http://127.0.0.1:9080/onvif/device_service\"," +
                    "\"user\": \"admin\", \"password\": \"admin\"," +
	                "\"reqclass\": \"SetSystemDateAndTime\"," +
                    "\"request\": {}" +
                  "}";
	Map<String, String> ctrl = new HashMap<>();
	ctrl.put("security", "digest");
	ctrl.put("debug", "false");
	String peekc = null, peekt = null, peekcTest = null;
	
	InvokeOperation onvifop = new InvokeOperation();
	
	//Gson gser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	
	ObjectMapper jser = new ObjectMapper();
	jser.enable(SerializationFeature.INDENT_OUTPUT);
	jser.registerModule(new JakartaXmlBindAnnotationModule());

	try {
	  JsonRequestResponse callo = JsonRequestResponse.create(call);
	  System.out.println("This should equal the device json details: " + callo.respjo);
	  
	  callo.response = onvifop.invoke(callo, true, ctrl);
	  
	  System.out.println("read in first callo response: " + callo.response); //read first callo response
	  //System.out.println("read in first callo class call: " + callo.response.getClass().getSimpleName());
	  //System.out.println("read in first callo response: " + callo.response.getClass().getName());
	  
	  peekc = callo.ser();
      System.out.println(peekc); //output of first response test.
	  
      peekcTest = callo.response.toString();
      System.out.println("tested the response object" + peekcTest);
	
      
	  JsonRequestResponse testo = JsonRequestResponse.create(test);
	  
	  Class savreq = null, savresp = null;
	
	  if (testo.request instanceof Class) {
	    savreq = (Class)testo.request;
	    testo.request = null;
	  }
	  if (testo.response instanceof Class) {
	    savresp = (Class)testo.response;
	    testo.response = null;
	  }
	
	  peekt = jser.writeValueAsString(testo);
	  System.out.println("Setter test: " + peekt);

	  peekc = callo.ser();
	  System.out.println(peekt);
    
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
    /* if (savreq != null) {
      try {
        Constructor ctor = savreq.getConstructor(null);
        
        Object o = ctor.newInstance(null);
        
        peek = ser.toJson(o);
        System.out.println(peek);
        
      } catch (Exception ex) {
        ex.printStackTrace();
    } */
  }
  
  /* static String getOperation(String operation, JsonRequestResponse jrro) {
	String res = null;
			
	Object[] op = OnvifOperations.getOperationPrototypes(operation);
	    
	if (op != null) {
	  jrro.reqclass = op[0].getClass().getSimpleName();
	  jrro.respclass = op[1].getClass().getSimpleName();
	  jrro.request = op[0];
	  jrro.response = op[1];
	}
	
	res = jrro.ser();
  } */
}
