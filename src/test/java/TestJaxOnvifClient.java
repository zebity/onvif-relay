/*
 @what Test onvif device client
 
*/

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onvif.ver10.Device;
import org.onvif.ver10.DeviceService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fence.util.ConfigurationData;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;

import onvif_relay.client.JaxOnvifClient;
import onvif_relay.client.OnvifAuthHandler;

public class TestJaxOnvifClient {
	
  public static void main(String [] args) {
	ConfigurationData conf = new ConfigurationData(args);
	
    try {
      System.out.println("Running test ...");
      
      String ipv4 =conf.getItem("onvif-device", "device");
      
      JaxOnvifClient onv = new JaxOnvifClient();
      
      String got = onv.getInfo(conf, ipv4, "test#1", "Device", "GetDeviceInformation", null);
      
      System.out.println("got: \"" + got + "\".");
      
      // ser.toJson(devs, new FileWriter(dumpFile));
      // ser.toJson(UIDGen, new FileWriter(dumpFile));
      /* String peek = ser.toJson(ipAddr);
      System.out.println(peek);
      
      peek = ser.toJson(dev);
      System.out.println(peek);
      
      DeviceInfo deserDev = new Gson().fromJson(peek, DeviceInfo.class);
      
      peek = ser.toJson(deserDev);
      System.out.println(peek); */
      
	} catch (Exception x) {
      x.printStackTrace();
	}
  }
}
