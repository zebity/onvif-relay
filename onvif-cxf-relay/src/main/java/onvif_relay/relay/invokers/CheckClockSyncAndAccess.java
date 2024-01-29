package onvif_relay.relay.invokers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetSystemDateAndTimeResponse;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.media.wsdl.Media;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.SystemDateTime;

import jakarta.xml.ws.Holder;
import jakarta.xml.ws.soap.SOAPFaultException;
import onvif_relay.relay.converters.JsonRequestResponse;

public class CheckClockSyncAndAccess {
  public CheckClockSyncAndAccess() {	  
  }
  
  public Object[] checkAccess(String addr, String user, String password) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
              "\"user\": \"" + user + "\", \"password\": \"" + password + "\"," +
		        "\"reqclass\": \"GetDeviceInformation\"," +
              "\"request\": { }" +
            "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", "digest");
    ctrl.put("debug", "false");
    boolean altAuth = false, authFault = false;


    InvokeOperation onvifop = new InvokeOperation();

    do {
      try {

  	    authFault = false;
  	
        JsonRequestResponse callo = JsonRequestResponse.create(call);
    
        Object[] proxy = onvifop.createSEI(callo, ctrl);
    
  	    GetDeviceInformationResponse resp = new GetDeviceInformationResponse();
  	    Holder<String> man = new Holder<>();
  	    Holder<String> mod = new Holder<>();
  	    Holder<String> firm = new Holder<>();
  	    Holder<String> serial = new Holder<>();
  	    Holder<String> hard = new Holder<>();
  	    ((Device)proxy[2]).GetDeviceInformation(man, mod, firm, serial, hard);
  	    resp.setManufacturer(man.value.toString());
  	    resp.setModel(mod.value.toString());
  	    resp.setFirmwareVersion(firm.value.toString());
  	    resp.setSerialNumber(serial.value.toString());
  	    resp.setHardwareId(hard.value.toString());
  	    callo.response = resp;
  	    res = new Object[3];
  	    res[0] = ctrl.get("security");
  	    res[1] = callo.ser();
  	    res[2] = resp;

      } catch (SOAPFaultException sex) {
        Iterator<QName> it = sex.getFault().getFaultSubcodes();
        if (it.hasNext()) {
          QName err = it.next();
        }
        if (sex.getMessage().contains("NotAuthorized")) {
          authFault = true;
          if (! altAuth) {
            altAuth = true;
            ctrl.put("security", "ws-security");
          } else {
            altAuth = false;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } while (authFault && altAuth);
  
    return res;
  }

  public Object[] checkClockSync(String addr, String user, String password, String sec) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
              "\"user\": \"" + user + "\", \"password\": \"" + password + "\"," +
		        "\"reqclass\": \"GetSystemDateAndTime\"," +
              "\"request\": { }" +
            "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", sec);
    ctrl.put("debug", "false");
    boolean authFault = false;


    InvokeOperation onvifop = new InvokeOperation();

    try {

  	  authFault = false;
  	
      JsonRequestResponse callo = JsonRequestResponse.create(call);
    
      Object[] proxy = onvifop.createSEI(callo, ctrl);
    
  	  GetSystemDateAndTimeResponse resp = new GetSystemDateAndTimeResponse();
  	  SystemDateTime dt = ((Device)proxy[2]).GetSystemDateAndTime();
  	  resp.setSystemDateAndTime(dt);
  	  callo.response = resp;
  	  res = new Object[3];
  	  res[0] = "synced";
  	  res[1] = callo.ser();
  	  res[2] = resp;

    } catch (SOAPFaultException sex) {
      Iterator<QName> it = sex.getFault().getFaultSubcodes();
      if (it.hasNext()) {
        QName err = it.next();
      }
      if (sex.getMessage().contains("NotAuthorized")) {
        authFault = true;

      }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return res;
  }
}
