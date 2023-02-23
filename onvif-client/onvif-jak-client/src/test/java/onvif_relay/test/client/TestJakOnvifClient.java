/**
 @what Test onvif device client
 
*/

package onvif_relay.test.client;

import fence.util.ConfigurationData;
import onvif_relay.client.jak.JakOnvifClient;

public class TestJakOnvifClient {
	
  public static void main(String [] args) {
	ConfigurationData conf = new ConfigurationData(args);
	
    try {
      System.out.println("Running test ...");
      
      String ipv4 =conf.getItem("onvif-device", "device");
      
      JakOnvifClient onv = new JakOnvifClient();
      
      String got = onv.getInfo(conf, ipv4, "test#1", "Device", "GetDeviceInformation", "{}");
      
      System.out.println("TestJakOnvifClient::main - got: \"" + got + "\".");
      
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
