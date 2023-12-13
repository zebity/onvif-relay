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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
import org.onvif.ver10.schema.NetworkInterface;

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
	
  Object invokeDevice(JsonRequestResponse target, boolean doClassify, Map<String, String> ctrl) {
    Object res = null;
    
    DeviceService dserv = new DeviceService();
    Device sei = dserv.getDevicePort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, target.target, target.user, target.password, ctrl)) {
    	  
    	try {
          res = invokeMethod(sei, useMethod, target);
          if (doClassify) {
            String[] cs = classify(target, useMethod);
            target.operationType = cs[0];
            target.voidOperation = cs[1];
          }
    	} catch (Exception ex) {
    	  ex.printStackTrace();
    	}
      }
    }
    
    return res;
  }
  
  Object invokeMedia(JsonRequestResponse target, boolean doClassify, Map<String, String> ctrl) {
    Object res = null;
    
    MediaService dserv = new MediaService();
    Media sei = dserv.getMediaPort();
    
    Object[] useMethod = discoverMethod(sei, target);
    
    if (useMethod != null) {
      if (setupService(sei, target.target, target.user, target.password, ctrl)) {
    	try {
      	  res = invokeMethod(sei, useMethod, target);
          if (doClassify) {
            String[] cs = classify(target, useMethod);
            target.operationType = cs[0];
            target.voidOperation = cs[1];
          }
    	} catch (Exception ex) {
    	  ex.printStackTrace();
    	}
      }    	
    }
    
    return res;
  }
  
  public Object invoke(JsonRequestResponse targetRequest, boolean doClassify, Map<String, String> ctrl) {
    Object res = null;

    String reqType = targetRequest.request.getClass().getPackageName();
    switch (reqType) {
      case OnvifOperations.DeviceType: res = invokeDevice(targetRequest, doClassify, ctrl);
                                       break;
      case OnvifOperations.MediaType: res = invokeMedia(targetRequest, doClassify, ctrl);
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
	Object[] strategy = new Object[]{"none", -1, -1, -1};
	
    List<Class<?>> params = new ArrayList<>();
    Class<?>[] plist = null;
    
    Field[] opFields = target.request.getClass().getDeclaredFields();
    if (opFields.length > 0) {
      strategy[0] = "request";
      strategy[1] = opFields.length;
      for (int i = 0; i < opFields.length; i++) {
        params.add(opFields[i].getType());
      }
    } else {
      strategy[1] = 0;
      if (target.response instanceof Class) {
    	Class respType = (Class)target.response;
        opFields = respType.getDeclaredFields();
        strategy[2] = opFields.length;
        for (int i = 0; i < opFields.length; i++) {
          params.add(opFields[i].getType());
        }
        switch (opFields.length) {
         case 0: strategy[0] = "empty";
        	     strategy[3] = 0;
                 break;
         case 1: strategy[0] = "empty-with-return";
                 strategy[3] = 1;
                 break;
         default: strategy[0] = "response";
                  strategy[3] = 0;
        }
      }
    }
    
    try {
      plist = new Class<?>[params.size()];
      if (strategy[0].equals("response")) {
        for (int i = 0; i < params.size(); i++) {
          // NOTE: template info is not maintained within VM runtime
          //         all objects wrapped via Holder<t> -> Holder<Object>
          plist[i] = Holder.class;
         
        }
      } else if (((String)strategy[0]).substring(0,5).equals("empty")) {
    	  plist = emptyArgs;
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
  
  String[] classify(JsonRequestResponse target, Object[] method) {
	String [] cs = new String[] { "action", "false" };
	Object[] strategy = (Object[])method[2];
	
    String prefix = target.reqclass.substring(0, 3).toLowerCase();
      
    switch (prefix) {
     case "get":
     case "set": cs[0] = prefix;
                 break;
    }
    if ((int)strategy[3] == 0) {
      cs[1] = "true";
    }
    return cs;
  }
  
  Object wrapResponse(JsonRequestResponse target, Object[] useMethod, Object got) throws Exception {
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
      Class<?>[] setArg = new Class<?>[1];
      String nm = null, setMethod = null, getMethod = null;
      Method setm = null, getm = null, addm = null;
      
      switch (outData.length) {
        case 0: break;
        case 1: nm = outData[0].getName();
        	    setMethod = "set" + nm.substring(0,1).toUpperCase() + nm.substring(1);
        	    setArg[0] = got.getClass();
        	    try {
                  setm = respo.getClass().getMethod(setMethod, setArg);
                  setm.invoke(respo, got);
        	    } catch (Exception ex) {
        	      // System.out.println(ex);
        	      System.out.println("INFO>> InvokeOperation::WrapResponse: no set, recovering using get+add.");
        	      getMethod = "get" + nm.substring(0,1).toUpperCase() + nm.substring(1);
        	      getm = respo.getClass().getMethod(getMethod, null);
        	      
        	      List<?> tlist = (List<?>)getm.invoke(respo, null);
        	      List<?> slist = (List<?>)got;
        	      
        	      if (slist.size() > 0) {
        	    	// setArg[0] = slist.get(0).getClass();
        	    	setArg[0] = Object.class;
        	        addm = tlist.getClass().getMethod("add", setArg);
        	        
        	        // tlist.addAll((Collection<?>) slist);
        	        for (int i = 0; i < slist.size(); i++) {
        	          addm.invoke(tlist, slist.get(i));
        	        }
        	      }
        	    }
                break;
        default: System.out.println("ERR>> InvokeOperation::wrapResponse - multiple fields: " + respo.getClass().getCanonicalName());
      }
      res = respo;
    }
	return res;
  }
  
  Object getResponseArgs(JsonRequestResponse target, Object[] useMethod, Object[] args) throws Exception {
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
    Object[] strategy = (Object[])useMethod[2];
    Method method = (Method)useMethod[0];
    Object[] args = new Object[plist.length];
    Method getm = null;
    String getMethod = null;
    
    if (strategy[0].equals("response")) {
    	
      Class<?>[] paramType = method.getParameterTypes();
      for (int i = 0; i < paramType.length; i++) {
        args[i] = paramType[i].getDeclaredConstructor().newInstance();
      }
      
      method.invoke(sei, args);
      res = getResponseArgs(target, useMethod, args);
      
    } else if (strategy[0].equals("request")) {
    	
      Field[] inputParam = target.request.getClass().getDeclaredFields();
      
      /* Map<String, Field> reqData = new HashMap<>();
      for (Field f: inputParam)
    	reqData.put(f.getName().toLowerCase(), f);     
      Parameter[] params = method.getParameters(); */
      
      for (int i = 0; i < inputParam.length; i++) {
    	// String name = params[i].getName().toLowerCase();
    	// args[i] = reqData.get(name);
    	Field fld = inputParam[i];

    	try {
  	      getMethod = "get" + fld.getName().substring(0,1).toUpperCase() + fld.getName().substring(1);
  	      getm = target.request.getClass().getMethod(getMethod, null);
  	      
	      List<?> tlist = (List<?>)getm.invoke(target.request, null);
	      args[i] = tlist;
    		
    	} catch (Exception ex) {
    	  args[i] = fld.get(target.request);
    	}
      }
      
      Object got = method.invoke(sei, args);
      res = wrapResponse(target, useMethod, got);
      
    } else if (((String)strategy[0]).substring(0,5).equals("empty")) {
    	
      Object got = method.invoke(sei, emptyParams);
      res = wrapResponse(target, useMethod, got);
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
