/*
 @what Test onvif device client
 
*/

package onvif_relay.client;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.onvif.ver10.Device;
import org.onvif.ver10.DeviceService;
import org.onvif.ver10.Media;
import org.onvif.ver10.MediaService;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fence.util.ConfigurationData;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.soap.SOAPBinding;
import onvif_relay.client.JakOnvifAuthHandler;

public class JakOnvifClient {
  public org.onvif.ver10.device.wsdl.ObjectFactory onvDevFact = new org.onvif.ver10.device.wsdl.ObjectFactory();
  public org.onvif.ver10.schema.ObjectFactory onvSchemaFact = new org.onvif.ver10.schema.ObjectFactory();

  public String getInfo(ConfigurationData confData, String ip, String hw_id, String reqType, String getthis, List<String> args) {
	String res = null;
	String urit = confData.getItem(hw_id, "device-service");
	String auth = confData.getItem(hw_id, "auth");

	String[] parts = urit.split(":");
	String [] cred = auth.split(":");
	
    String reqURL = new String(parts[0] + "://" + ip + ":" + parts[1]);
	      
	if (reqURL != null) {
	    	  
	  try {
		
	    String result = null;
		DeviceService devSrv = null;
		Device dev = null;
		MediaService mediaSrv = null;
		Media media = null;
		URL devURL = new URL(reqURL);
		    
		// Map<String, List<String>> Headers = new HashMap<String, List<String>>();
		// List<String> HdrVals = new ArrayList<>(); HdrVals.add("text/xml");
		// Headers.put("Content-Type", HdrVals);

		if (reqType.equals("Media")) {
		  mediaSrv = new MediaService();
		  media = mediaSrv.getMediaPort();
      
		  if (media instanceof BindingProvider) {
		    BindingProvider bp = (BindingProvider)media;
		    Binding binding = bp.getBinding();
		    if (binding instanceof SOAPBinding) {
			  SOAPBinding soapBinding = (SOAPBinding)binding;
			  Set<String> roles = soapBinding.getRoles();
			  System.out.println("DBG>> DeviceOnvifDetails::getOnvifDeviceService - Roles: " + roles.toString());
			}
		    List<Handler> handList = binding.getHandlerChain();
		    handList.add(new JakOnvifAuthHandler());
		    binding.setHandlerChain(handList);
		    result = SOAPWSMediaReqest(getthis, args, media);
		  }
		} else if (reqType.equals("Device") || reqType.equals("PreAuth")) {
		  devSrv = new DeviceService();
		  dev = devSrv.getDevicePort();
		      
		  if (dev instanceof BindingProvider) {
			BindingProvider bp = (BindingProvider)dev;
		    Binding binding = bp.getBinding();
		    if (binding instanceof SOAPBinding) {
		      SOAPBinding soapBinding = (SOAPBinding)binding;
		      Set<String> roles = soapBinding.getRoles();
		      System.out.println("DBG>> DeviceOnvifDetails::getOnvifDeviceService - Roles: " + roles.toString());
		    }
		    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, devURL.toString());
		    // bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, Headers);
		    // bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, reqURL[1]);
		    // bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, reqURL[2]);
		    List<Handler> handList = binding.getHandlerChain();
		    handList.add(new JakOnvifAuthHandler());
		    binding.setHandlerChain(handList);
		        
			result = SOAPWSDeviceReqest(getthis, args, dev);
		  }
		}
	  	if (result != null) {
	  	  res = new String("{\"" + reqURL + ": " + result + "}");
	  	  System.out.println(res);
	  	}
      } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
      }
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
