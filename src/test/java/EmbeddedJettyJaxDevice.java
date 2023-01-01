/**
@what Embedded Jetty JAVAX JAX-WS Device Simulator 

@note: See Eclipse Jetty: Programming Guide

*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jetty.server.Server;

import javax.xml.ws.Endpoint;

import onvif_relay.service.JaxDeviceImpl;
import fence.util.ConfigurationData;

public class EmbeddedJettyJaxDevice {
	
  public static void main(String[] args) throws Exception {
    ConfigurationData confData = new ConfigurationData(args);

    String srvPort = confData.getItem("onvif-device", "port");
	String port = confData.getItem("onvif-device", "device-port");
	String request = confData.getItem("onvif-device", "request");
	
    try {
        
      System.out.println("Starting the Jetty server on port: " + srvPort + " onvif mgt on: " + port);
        
      Server server = new Server(Integer.parseInt(srvPort));
      
      JaxDeviceImpl device = new JaxDeviceImpl();
      
      System.setProperty("com.sun.net.httpserver.HttpServerProvider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      // System.setProperty("jakarta.xml.ws.spi.Provider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      
      String uri = "http://127.0.0.1:" + port + request;
      // Endpoint ep = Endpoint.create(uri, device);
      Endpoint ep = Endpoint.publish(uri, device);
      
      server.start();
      
      // consoleLoop(DevManager);
      
      server.join();
      System.out.println("Stopped the simple server...");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  static public void consoleLoop() {
    boolean stopServer = false;
    String line, path;

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	   
    do {
      System.out.print("Console: [r]eload [s]top: ");
	       
      try {
        line = br.readLine();
        if (line.equals("s")) {
   	      stopServer = true;
        } else if (line.equals("r")) {
            // reload config...
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } while (! stopServer);     
  }
}
