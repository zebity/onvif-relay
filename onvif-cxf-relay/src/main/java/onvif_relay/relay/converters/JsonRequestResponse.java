/**
 * @what - Simple JSON Onvif send/receive encapsulation class
 */

package onvif_relay.relay.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;


public class JsonRequestResponse {
  public String target,
         user,
         password,
         reqclass,
         respclass,
         operationType,
         voidOperation;
  public Object request,
                response;
  
  
  public String ser() {
	String res = null;
	Class<?> savReq = null, savResp = null;
	
	try {
	  ObjectMapper ser = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerModule(new JakartaXmlBindAnnotationModule());
	  // Gson ser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	  
	  if (request != null) {
		if (request instanceof Class) {
		  savReq = (Class<?>)request;
		  request = null;
		} else {
	      reqclass = request.getClass().getSimpleName();
		}
	  }
	  
	  if (response != null) {
		if (response instanceof Class) {
		  savResp = (Class<?>)request;
		  response = null;
		} else {
		  respclass = response.getClass().getSimpleName();
		}
	  }
	  
	  // res = ser.toJson(this);
	  res = ser.writeValueAsString(this);
	  
	  if (savReq != null) {
	    request = savReq;
	  }
	  
	  if (savResp != null) {
	    response = savResp;
	  }
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
	return res;
  }
  
  public static Object[] createRequest(String reqClass, String jsonStr) throws JsonMappingException, JsonProcessingException {
    Object[] res = null;
  
    Object[] protos = OnvifOperations.getOperationPrototypes(reqClass);
    Class<?> opClass = protos[0].getClass();
    
    if (opClass != null) {
      res = new Object[2];
      res[0] = new ObjectMapper().readValue(jsonStr, opClass);
      res[1] = protos[1].getClass();
    }
    return res;
  }
  
  public void maskCredentials() {
	user = null;
	password = null;
  }
  
  public static JsonRequestResponse create(String jsonStr) throws JsonMappingException, JsonProcessingException {
    JsonRequestResponse res = null;
    
    String target = null, user = null, password = null, reqClass = null, respClass= null,
    	   operationType = null, voidOperation = null;
    JsonNode reqjo = null, respjo = null;
    
    JsonNode jo = new ObjectMapper().readTree(jsonStr);
    if (jo != null) {
    		  			
      JsonNode jp = jo.get("target");
      if (jp != null)
    	target = jp.textValue();
    		  			
      jp = jo.get("user");
      if (jp != null)
    	user = jp.textValue();
    		  			
      jp = jo.get("password");
      if (jp != null)
    	password = jp.textValue();
    		  			
      jp = jo.get("reqclass");
      if (jp != null)
    	reqClass = jp.textValue();
    		  			  
      jp = jo.get("respclass");
      if (jp != null)
    	respClass = jp.textValue();
      
      jp = jo.get("operationType");
      if (jp != null)
        operationType = jp.textValue();
      
      jp = jo.get("voidOperation");
      if (jp != null)
        voidOperation = jp.textValue();
    		 			
      reqjo = jo.get("request");
      respjo = jo.get("response");
      
      Object[] protos = OnvifOperations.getOperationPrototypes(reqClass);
      
      if (protos != null) {
    	  
        res = new JsonRequestResponse();
        res.target = target;
        res.user = user;
        res.password = password;
        res.reqclass = reqClass;
        res.operationType = operationType;
        res.voidOperation = voidOperation;

        if (reqjo == null) {
          res.request = protos[0].getClass(); 
        } else {
          Object deserreq = new ObjectMapper().treeToValue(reqjo, protos[0].getClass());
          res.request = deserreq;
        }

        if (respjo == null) {
          res.response = protos[1].getClass();  
        } else {
          Object deserresp = new ObjectMapper().treeToValue(respjo, protos[1].getClass());
          res.response = deserresp;
          res.respclass = deserresp.getClass().getSimpleName();
        }
      }
    }
	return res;  
  }
}