/*
 @what Test onvif device client
 
*/

package onvif_relay.client.jak;

import java.util.HashMap;
import java.util.Map;
import fence.util.ConfigurationData;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.invokers.InvokeOperation;

public class JakOnvifClient {
  public org.onvif.ver10.device.wsdl.ObjectFactory onvDevFact = new org.onvif.ver10.device.wsdl.ObjectFactory();
  public org.onvif.ver10.schema.ObjectFactory onvSchemaFact = new org.onvif.ver10.schema.ObjectFactory();
  InvokeOperation onvifOps = new InvokeOperation();
		  
  public String getInfo(ConfigurationData confData, String ip, String hw_id, String reqType, String getthis, String reqData) {
	String res = null;
	String durit = confData.getItem(hw_id, "device-service");
	String murit = confData.getItem(hw_id, "media-service");
	String auth = confData.getItem(hw_id, "auth");
	String security = confData.getItem(hw_id, "security");
	String dump = confData.getItem(hw_id, "dump");
	String soapver = confData.getItem(hw_id, "soap-ver");
    String cxf = confData.getItem(hw_id, "cxf");
	Map<String, String> ctrl = new HashMap<>();
	
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
	  
	  ctrl.put("debug", "false");
	  ctrl.put("security", security);
	  ctrl.put("cxf", cxf);
	  ctrl.put("soap-ver", soapver);
	  ctrl.put("dump", dump);
	  	  
	  try {
		
	    JsonRequestResponse reqObj = new JsonRequestResponse();
		reqObj.user = cred[0];
		reqObj.password = cred[1];
		reqObj.reqclass = getthis;
		    
		if (reqType.equals("Media")) {
		  reqObj.target = mreqURL;
		} else if (reqType.equals("Device") || reqType.equals("PreAuth")) {
		  reqObj.target = dreqURL;
		}
		
        Object[] reqnclass = JsonRequestResponse.createRequest(reqObj.reqclass, reqData);
		reqObj.request = reqnclass[0];
		reqObj.response = reqnclass[1];

		
		reqObj.response = onvifOps.invoke(reqObj, true, ctrl);
		
		reqObj.maskCredentials();
		
		res = reqObj.ser();

	  	System.out.println(res);
	  	
      } catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
      }
    }
	return res;
  }
  
}
