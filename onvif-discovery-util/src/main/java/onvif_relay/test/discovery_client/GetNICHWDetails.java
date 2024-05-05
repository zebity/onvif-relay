package onvif_relay.test.discovery_client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

public class GetNICHWDetails {
  public static void main(String[] args) {
      try {
          InetAddress localHost = InetAddress.getLocalHost();
          InetAddress myip = InetAddress.getByName("192.168.73.140");
          NetworkInterface ni = NetworkInterface.getByInetAddress(myip);
          Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
          
          if (!Objects.isNull(ni)) {
              byte[] hardwareAddress = ni.getHardwareAddress();
              String[] hexadecimal = new String[hardwareAddress.length];
              for (int i = 0; i < hardwareAddress.length; i++) {
                  hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
              }
              String macAddress = String.join("-", hexadecimal);
              //hardwareInfoDTO.setMacAddress(macAddress);
              //logger.info("Host MAC Address: {}", macAddress);
          }
      } catch (Exception e) {
    	e.printStackTrace();
          //logger.error("Exception Occurred while getting Host MAC Address", e);
      }
  }

}
