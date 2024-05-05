package onvif_relay.relay.invokers;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import org.onvif.ver10.schema.DateTime;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.SystemDateTime;

import jakarta.xml.ws.Holder;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.soap.SOAPFaultException;
import onvif_relay.relay.converters.JsonRequestResponse;

public class CheckClockSyncAndAccess {
  public CheckClockSyncAndAccess() {	  
  }
  
  public Object[] checkAccess(String addr, String user, String password, boolean SSLValidate) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
              "\"user\": \"" + user + "\", \"password\": \"" + password + "\"," +
		        "\"reqclass\": \"GetDeviceInformation\"," +
              "\"request\": { }" +
            "}";
	JsonRequestResponse callo = null;
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", "digest");
    ctrl.put("debug", "false");
    
    if (addr != null) {
      if (! SSLValidate && addr.substring(0,5).equals("https")) {
        ctrl.put("sslvalidate", "false");
      } else {
    	ctrl.put("sslvalidate", "true");
      }
    }
    
    boolean altAuth = false, authFault = false;


    InvokeOperation onvifop = new InvokeOperation();

    do {
      try {

  	    authFault = false;
  	
        callo = JsonRequestResponse.create(call);
    
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
        	res = new Object[3];
          	res[0] = ctrl.get("security");
          	res[1] = callo.ser();
          	res[2] = sex.getMessage();
            altAuth = false;
          }
        }
      } catch (WebServiceException wex) {
        // Assume digest with wrong password
      	res = new Object[3];
      	res[0] = ctrl.get("security");
      	res[1] = callo.ser();
      	res[2] = "Authorization loop detected: Invalid user/password";
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } while (authFault && altAuth);
  
    return res;
  }

  public Object[] checkClockSync(String addr, String user, String password, String sec, boolean SSLValidate) {
	Object[] res = null;
	String call = "{\"target\": \"" + addr + "\"," +
              "\"user\": \"" + user + "\", \"password\": \"" + password + "\"," +
		        "\"reqclass\": \"GetSystemDateAndTime\"," +
              "\"request\": { }" +
            "}";
    Map<String, String> ctrl = new HashMap<>();
    ctrl.put("security", sec);
    ctrl.put("debug", "false");
    JsonRequestResponse callo = null;

    if (addr != null) {
      if (! SSLValidate && addr.substring(0,5).equals("https")) {
        ctrl.put("sslvalidate", "false");
      } else {
      	ctrl.put("sslvalidate", "true");
      }
    }

    InvokeOperation onvifop = new InvokeOperation();

    try {
  	
      callo = JsonRequestResponse.create(call);
    
      Object[] proxy = onvifop.createSEI(callo, ctrl);

  	  Clock utcClock = Clock.systemUTC();
  	  GetSystemDateAndTimeResponse resp = new GetSystemDateAndTimeResponse();
  	  SystemDateTime dvdt = ((Device)proxy[2]).GetSystemDateAndTime();
  	  ZonedDateTime tick = ZonedDateTime.now(utcClock);
  	  resp.setSystemDateAndTime(dvdt);

  	  String see = tick.toString();
  	  DateTime utc =  dvdt.getUTCDateTime();
  	  ZonedDateTime zdt = ZonedDateTime.of(utc.getDate().getYear(), utc.getDate().getMonth(),
  			                    utc.getDate().getDay(), utc.getTime().getHour(),
  			                    utc.getTime().getMinute(), utc.getTime().getSecond(), 0,
  			                    ZoneOffset.UTC);

  	  callo.response = resp;
  	  res = new Object[3];
  	  // get time difference in seconds
  	  long diff = (zdt.toInstant().toEpochMilli() - tick.toInstant().toEpochMilli()) / 1000;
  	  res[0] = diff;
  	  res[1] = callo.ser();
  	  res[2] = resp;

    } catch (SOAPFaultException sex) {
      Iterator<QName> it = sex.getFault().getFaultSubcodes();
      if (it.hasNext()) {
        QName err = it.next();
      }
      if (sex.getMessage().contains("NotAuthorized")) {
        res = new Object[3];
        res[0] = null;
        res[1] = callo.ser();
        res[2] = sex.getMessage();
      }
    } catch (WebServiceException wex) {
        // Assume digest with wrong password
      	res = new Object[3];
      	res[0] = null;
      	res[1] = callo.ser();
      	res[2] = "Authorization loop detected: Invalid user/password";
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return res;
  }
}
