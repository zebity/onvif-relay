/**
 * @what - Simple JSON Onvif send/receive encapsulation class
 */

package onvif_relay.relay.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonRequestResponse {
  public String target,
         user,
         password,
         reqclass,
         respclass;
  public Object request,
                response;
  
  
  public String ser() {
	String res = null;
	Class<?> savReq = null, savResp = null;
	
	try {
	  Gson ser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	  
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
	  
	  res = ser.toJson(this);
	  
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
  
  public static Object[] createRequest(String reqClass, String jsonStr) {
    Object[] res = null;
  
    Object[] protos = OnvifOperations.getOperationPrototypes(reqClass);
    Class<?> opClass = protos[0].getClass();
    
    if (opClass != null) {
      res = new Object[2];
      res[0] = new Gson().fromJson(jsonStr, opClass);
      res[1] = protos[1].getClass();
    }
    return res;
  }
  
  public static JsonRequestResponse create(String jsonStr) {
    JsonRequestResponse res = null;
    
    String target = null, user = null, password = null, reqClass = null, respClass= null;
    JsonObject reqjo = null, respjo = null;
    
    JsonObject jo = JsonParser.parseString(jsonStr).getAsJsonObject();
    if (jo != null) {
    		  			
      JsonPrimitive jp = jo.getAsJsonPrimitive("target");
      if (jp != null)
    	target = jp.getAsString();
    		  			
      jp = jo.getAsJsonPrimitive("user");
      if (jp != null)
    	user = jp.getAsString();
    		  			
      jp = jo.getAsJsonPrimitive("password");
      if (jp != null)
    	password = jp.getAsString();
    		  			
      jp = jo.getAsJsonPrimitive("reqclass");
      if (jp != null)
    	reqClass = jp.getAsString();
    		  			  
      jp = jo.getAsJsonPrimitive("respclass");
      if (jp != null)
    	respClass = jp.getAsString();
    		 			
      reqjo = jo.getAsJsonObject("request");
      respjo = jo.getAsJsonObject("response");
      
      Object[] protos = OnvifOperations.getOperationPrototypes(reqClass);
      
      if (protos != null) {
    	  
        res = new JsonRequestResponse();
        res.target = target;
        res.user = user;
        res.password = password;
        res.reqclass = reqClass;

        if (reqjo == null) {
          res.request = protos[0].getClass(); 
        } else {
          Object deserreq = new Gson().fromJson(reqjo, protos[0].getClass());
          res.request = deserreq;
        }

        if (respjo == null) {
          res.response = protos[1].getClass();  
        } else {
          Object deserresp = new Gson().fromJson(respjo, protos[1].getClass());
          res.response = deserresp;
          res.respclass = deserresp.getClass().getSimpleName();
        }
      }
    }
	return res;  
  }
}