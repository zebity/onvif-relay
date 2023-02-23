/**
 @what Invoke ONVIF WSDL methods using Java reflection to allow automatiic JAX-WS <-> Restful relay

 @note - this is potentially buggy as it assumes that the introspective "getFields"
           returns with order consist with method arguments list.
           Likely needs to be revisited to get info from annotations or
           compile with -parameters flag to ensure paramter names get included in image
 */
package onvif_relay.relay.invokers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.converters.OnvifOperations;


public class InvokeOperation {
  static Class<?>[] emptyArgs = {};
  static Object[] emptyParams = {};
	
  Object invokeDevice(JsonRequestResponse target, Map<String, String> ctrl) {
    Object res = null;
    
    DeviceService dserv = new DeviceService();
    Device sei = dserv.getDevicePort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, target.target, target.user, target.password, ctrl)) {
    	  
    	try {
          res = invokeMethod(sei, useMethod, target);
    	} catch (Exception ex) {
    	  ex.printStackTrace();
    	}
      }
    }
    
    return res;
  }
  
  Object invokeMedia(JsonRequestResponse target, Map<String, String> ctrl) {
    Object res = null;
    
    MediaService dserv = new MediaService();
    Media sei = dserv.getMediaPort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, target.target, target.user, target.password, ctrl)) {
      	  
      }    	
    }
    
    return res;
  }
  
  public Object invoke(JsonRequestResponse targetRequest, Map<String, String> ctrl) {
    Object res = null;

    String reqType = targetRequest.request.getClass().getPackageName();
    switch (reqType) {
      case OnvifOperations.DeviceType: res = invokeDevice(targetRequest, ctrl);
                                       break;
      case OnvifOperations.MediaType: res = invokeMedia(targetRequest, ctrl);
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
	
    List<Class<?>> params = new ArrayList<>();
    Class<?>[] plist = null;
    
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
      } else {
        strategy = "empty";
      }
    }
    
    try {
      plist = new Class<?>[params.size()];
      if (strategy.equals("response")) {
        for (int i = 0; i < params.size(); i++) {
          // NOTE: template info is not maintained within VM runtime
          //         all objects wrapped via Holder<t> -> Holder<Object>
          plist[i] = Holder.class;
         
        }
      } else {
    	for (int i = 0; i < params.size(); i++) {
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
  
  Object wrapResponse(JsonRequestResponse target, Method method, Object got) throws Exception {
	Object res = null;
	
    Class<?> cs = null;
    if (target.response instanceof Class<?>) {
      cs = (Class<?>)target.response;
    } else if (target.response != null) {
      cs = target.response.getClass();
    }
    
    Object respo = cs.getDeclaredConstructor().newInstance();
    if (respo != null) {
  	  
      Field[] outData = respo.getClass().getDeclaredFields();
      
      if (outData.length > 1) {
    	System.out.println("ERR>> InvokeOperation::wrapResponse - multiple fields: " + respo.getClass().getCanonicalName());
      } else {
        outData[0].set(respo, got);
        res = respo;
      }
      res = respo;
    }
	return res;
  }
  Object getResponseArgs(JsonRequestResponse target, Method method, Object[] args) throws Exception {
	Object res = null;
	
    Class<?> cs = null;
    if (target.response instanceof Class<?>) {
      cs = (Class<?>)target.response;
    } else if (target.response != null) {
      cs = target.response.getClass();
    }
    
    Object respo = cs.getDeclaredConstructor().newInstance();
    if (respo != null) {
  	  
      Field[] outParam = respo.getClass().getDeclaredFields();
      /* Map<String, Field> respData = new HashMap<>();
      for (Field f: outParam)
        respData.put(f.getName().toLowerCase(), f);
        
      Parameter[] params = method.getParameters(); */
      
      if (outParam.length == args.length) {
        
    	Class<?>[] setArg = new Class<?>[1];
    	Object[] value = new Object[1];
        for (int i = 0; i < args.length; i++) {
          String nm = outParam[i].getName();
          if (args[i] instanceof Holder<?>) {
            setArg[0] = ((Holder<?>)args[i]).value.getClass();
            value[0] = ((Holder<?>)args[i]).value;
          } else {
        	setArg[0] = args[i].getClass();
        	value[0] = args[i];
          }
          String setMethod = "set" + nm.substring(0,1).toUpperCase() + nm.substring(1);
          Method setm = respo.getClass().getMethod(setMethod, setArg);
          setm.invoke(respo, value);
        }
        res = respo;
      } else {
        System.out.println("ERR>> InvokeOperation: '" + respo.getClass().getCanonicalName() +
        		           "' - responce fields[" +
                           Integer.toString(outParam.length) + "] != args[" +
                           Integer.toString(args.length) + ".");
      }
    }
	return res;
  }
  
  Object invokeMethod(Object sei, Object[] useMethod, JsonRequestResponse target) throws Exception {
    Object res = null;
    Class<?>[] plist = (Class[])useMethod[1];
    String strategy = (String)useMethod[2];
    Method method = (Method)useMethod[0];
    Object[] args = new Object[plist.length];
    
    if (strategy.equals("response")) {
    	
      Class<?>[] paramType = method.getParameterTypes();
      for (int i = 0; i < paramType.length; i++) {
        args[i] = paramType[i].getDeclaredConstructor().newInstance();
      }
      
      method.invoke(sei, args);
      
      res = getResponseArgs(target, method, args);
      
    } else if (strategy.equals("request")) {
    	
      Field[] inputParam = target.request.getClass().getDeclaredFields();
      /* Map<String, Field> reqData = new HashMap<>();
      for (Field f: inputParam)
    	reqData.put(f.getName().toLowerCase(), f);
      
      Parameter[] params = method.getParameters(); */
      for (int i = 0; i < inputParam.length; i++) {
    	// String name = params[i].getName().toLowerCase();
    	// args[i] = reqData.get(name);
    	args[i] = inputParam[i];
      }
      
      Object got = method.invoke(sei, args);
      res = wrapResponse(target, method, got);
      
    } else if (strategy.equals("empty")) {
    	
      Object got = method.invoke(sei, emptyParams);
      res = wrapResponse(target, method, got);

    }
    
	return res;  
  }
  
  boolean setupService(Object sei, String uri, String user, String password, Map<String, String> ctrl) {
    boolean res = false;
    
    String security = ctrl.get("security");
	String debug = ctrl.get("debug");
	
	if (sei instanceof BindingProvider) {
	  BindingProvider bp = (BindingProvider)sei;
	  Binding binding = bp.getBinding();
	  
	  if (debug != null && debug.equals("true") && binding instanceof SOAPBinding) {
		SOAPBinding soapBinding = (SOAPBinding)binding;
		Set<String> roles = soapBinding.getRoles();
		System.out.println("DBG>> InvokeOperation::setupService - Roles: " + roles.toString());
	  }
	  
	  bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, uri);
			    
	  if (security == null || security.equals("basic")) {
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	  } else if (security != null && security.equals("digest")) {
	    // Note this code is cxf specific
		Client client = ClientProxy.getClient(sei);
		HTTPConduit httpo = (HTTPConduit)client.getConduit();
		AuthorizationPolicy authPolicy = new AuthorizationPolicy();
		authPolicy.setAuthorizationType("Digest");
		authPolicy.setUserName(user);
		authPolicy.setPassword(password);
		httpo.setAuthorization(authPolicy);           
	  }
	  
	  if (debug.equals("true")) {
		List<Handler> handList = binding.getHandlerChain();
		handList.add(new JakOnvifAuthHandler());
		binding.setHandlerChain(handList);
	  }  
	  res = true;
	}
	return res;
  }
}
