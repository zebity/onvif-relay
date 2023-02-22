package onvif_relay.relay.invokers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.DeviceService;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.media.wsdl.MediaService;

import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.soap.SOAPBinding;
import onvif_relay.client.jak.JakOnvifAuthHandler;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.converters.OnvifOperations;


public class InvokeOperation {
	
  Object invokeDevice(JsonRequestResponse target) {
    Object res = null;
    
    DeviceService dserv = new DeviceService();
    Device sei = dserv.getDevicePort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, "digest", target.target, target.user, target.password)) {
 
      }
    }
    
    return res;
  }
  
  Object invokeMedia(JsonRequestResponse target) {
    Object res = null;
    
    MediaService dserv = new MediaService();
    Media sei = dserv.getMediaPort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, "digest", target.target, target.user, target.password)) {
      	  
      }    	
    }
    
    return res;
  }
  
  public Object invoke(JsonRequestResponse targetRequest) {
    Object res = null;

    String reqType = targetRequest.request.getClass().getPackageName();
    String op = targetRequest.respclass;
    
    switch (reqType) {
      case OnvifOperations.DeviceType: res = invokeDevice(targetRequest);
                                       break;
      case OnvifOperations.MediaType: res = invokeMedia(targetRequest);
                                      break;
    }
    
    
	return res;
  }
 
  
  Object[] discoverMethod(Object sei, JsonRequestResponse target) {
  /* Can have:
   *   - inputs via request parameters with return via single object (GetStreamUri) or 
   *   - empty request returning multiple items, via object reference args (GetDeviceInformation) or
   *   - empty request returning single object via method return
   *   
   *   Assume: Request Object and Field return correct order...
   * 
   */
	Object[] res = null;
	String strategy = "none";
	
    Object respo = null;
    List<Class> params = new ArrayList<>();
    Class[] plist = null;
    
    Field[] reqParams = target.request.getClass().getDeclaredFields();
    if (reqParams.length > 0) {
      strategy = "request";
      for (int i = 0; i < reqParams.length; i++) {
        params.add(reqParams[i].getType());
      }
    } else {
      if (target.response instanceof Class) {
    	Class respType = (Class)target.response;
        reqParams = respType.getDeclaredFields();
        for (int i = 0; i < reqParams.length; i++) {
          params.add(reqParams[i].getType());
        }
        if (reqParams.length > 1) {
          strategy = "response";
        } else {
          strategy = "empty";
        }
      }
    }
    
    try {
      plist = new Class[params.size()];
      for (int i = 0; i < params.size(); i++) {
        if (strategy.equals("response")) {
          Class t = params.get(i);
          if (t == String.class) {
        	plist[i] = Holder.class;
          } else {
        	plist[i] = t;
          }
        } else {
          plist[i] = params.get(i);
        }
      }
      Method op = sei.getClass().getDeclaredMethod(target.reqclass, plist);
      res = new Object[]{ op, plist, strategy};
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return res;
  }
  
  boolean setupService(Object sei, String security, String uri, String user, String password) {
    boolean res = false;
	    
	    if (sei instanceof BindingProvider) {
		  BindingProvider bp = (BindingProvider)sei;
		  Binding binding = bp.getBinding();
		  if (binding instanceof SOAPBinding) {
			SOAPBinding soapBinding = (SOAPBinding)binding;
			Set<String> roles = soapBinding.getRoles();
			System.out.println("DBG>> InvokeOperation::setupService - Roles: " + roles.toString());
		  }
		  bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, uri);
			    
	      if (security != null && security.equals("basic")) {
			bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
			bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	      } else if (security.equals("digest")) {
	        // Note this code is cxf specific
			Client client = ClientProxy.getClient(sei);
			HTTPConduit httpo = (HTTPConduit)client.getConduit();
			AuthorizationPolicy authPolicy = new AuthorizationPolicy();
			authPolicy.setAuthorizationType("Digest");
			authPolicy.setUserName(user);
			authPolicy.setPassword(password);
			httpo.setAuthorization(authPolicy);           
		  }
	          
		  List<Handler> handList = binding.getHandlerChain();
		  handList.add(new JakOnvifAuthHandler());
		  binding.setHandlerChain(handList);
		  
		  res = true;
	    }
	    return res;
	  }
}
