/*
 @what Test onvif device client
 
*/

package onvif_relay.client.jak;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.DeviceService;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.media.wsdl.MediaService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fence.util.ConfigurationData;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.soap.SOAPBinding;

public class JakOnvifClient {
  public org.onvif.ver10.device.wsdl.ObjectFactory onvDevFact = new org.onvif.ver10.device.wsdl.ObjectFactory();
  public org.onvif.ver10.schema.ObjectFactory onvSchemaFact = new org.onvif.ver10.schema.ObjectFactory();

  public String getInfo(ConfigurationData confData, String ip, String hw_id, String reqType, String getthis, List<String> args) {
	String res = null;
	String durit = confData.getItem(hw_id, "device-service");
	String murit = confData.getItem(hw_id, "media-service");
	String auth = confData.getItem(hw_id, "auth");
	String security = confData.getItem(hw_id, "security");
	String dump = confData.getItem(hw_id, "dump");
	String soapver = confData.getItem(hw_id, "soap-ver");
    String cxf = confData.getItem(hw_id, "cxf");
	String soapbinding = null;
	
	if (dump != null && dump.equals("true")) {
	  System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
	}

    if (cxf == null) {
      cxf = "false";
    }

	String[] dparts = durit.split(":");
	String[] mparts = murit.split(":");
	String [] cred = auth.split(":");
	
    String dreqURL = new String(dparts[0] + "://" + ip + ":" + dparts[1]);	
    String mreqURL = new String(mparts[0] + "://" + ip + ":" + mparts[1]);
	      
	if (dreqURL != null && mreqURL != null) {
		
	  if (soapver != null) {
		switch (soapver) {
		  case "12": soapbinding = SOAPBinding.SOAP12HTTP_BINDING;
		        	 break;
		  case "11": soapbinding = SOAPBinding.SOAP11HTTP_BINDING;
		             break;
		  default:
		}
	  }
	    	  
	  try {
		
	    String result = null;
		DeviceService devSrv = null;
		Device dev = null;
		MediaService mediaSrv = null;
		Media media = null;
		URL devURL = new URL(dreqURL);
		URL medURL = new URL(mreqURL);
		    
		if (reqType.equals("Media")) {

		  mediaSrv = new MediaService();
		  media = mediaSrv.getMediaPort();
		  
          if (setupService(media, security, medURL, cred)) {
		    result = SOAPWSMediaReqest(getthis, args, media);
		  }
		} else if (reqType.equals("Device") || reqType.equals("PreAuth")) {
			

		  devSrv = new DeviceService();
		  dev = devSrv.getDevicePort();
		  
		  if (setupService(dev, security, devURL, cred)) {		        
			result = SOAPWSDeviceReqest(getthis, args, dev);
		  }
		}
	  	if (result != null) {
	  	  res = new String("{\"" + dreqURL + ": " + result + "}");
	  	  System.out.println(res);
	  	}
      } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
      }
    }
	return res;
  }
  
  boolean setupService(Object sei, String security, URL uri, String[] cred) {
    boolean res = false;
    
    if (sei instanceof BindingProvider) {
	  BindingProvider bp = (BindingProvider)sei;
	  Binding binding = bp.getBinding();
	  if (binding instanceof SOAPBinding) {
		SOAPBinding soapBinding = (SOAPBinding)binding;
		Set<String> roles = soapBinding.getRoles();
		System.out.println("DBG>> DeviceOnvifDetails::setupService - Roles: " + roles.toString());
	  }
	  bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, uri.toString());
		    
      if (security != null && security.equals("basic")) {
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, cred[0]);
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, cred[1]);
      } else if (security.equals("digest")) {
        // Note this code is cxf specific
		Client client = ClientProxy.getClient(sei);
		HTTPConduit httpo = (HTTPConduit)client.getConduit();
		AuthorizationPolicy authPolicy = new AuthorizationPolicy();
		authPolicy.setAuthorizationType("Digest");
		authPolicy.setUserName(cred[0]);
		authPolicy.setPassword(cred[1]);
		httpo.setAuthorization(authPolicy);           
	  }
          
	  List<Handler> handList = binding.getHandlerChain();
	  handList.add(new JakOnvifAuthHandler());
	  binding.setHandlerChain(handList);
	  
	  res = true;
    }
    return res;
  }
  
  public String SOAPWSDeviceReqest(String getthis, List<String> args, Device dev) {
    String res = null;
	    
	switch (getthis) {
	  // case "GetCapabilities": res = getCapabilities(args, dev);
	  //                        break;
	  // case "GetSystemDateAndTime": res = getSystemDateAndTime(args, dev);
	  //	                           break;
	  case "GetDeviceInformation": res = GetDeviceInformation(args, dev);
	                               break;
	  // case "GetNetworkInterfaces": res = getNetworkInterfaces(args, dev);
	  //                                 break;
	  default:
	}
	return res;
  }

  public String SOAPWSMediaReqest(String getthis, List<String> args, Media dev) {
	String res = null;

    return res;
  }
  
  public String GetDeviceInformation(List<String> args, Device dev) {
    String res = null;    
	    
	try {

	  GetDeviceInformationResponse resp = onvDevFact.createGetDeviceInformationResponse();
	      
	  Holder<String> manu = new Holder<>();
	  Holder<String> firm = new Holder<>();
	  Holder<String> hid = new Holder<>();
	  Holder<String> model = new Holder<>();
	  Holder<String> serial = new Holder<>();
	      
	  dev.GetDeviceInformation(manu, model, firm, serial, hid);
	      
	  resp.setFirmwareVersion(firm.value);
	  resp.setHardwareId(hid.value);
	  resp.setManufacturer(manu.value);
	  resp.setModel(model.value);
	  resp.setSerialNumber(serial.value);
	      
	  Gson ser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	  res = ser.toJson(resp);
	      
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
	    
	return res;
  }
}
