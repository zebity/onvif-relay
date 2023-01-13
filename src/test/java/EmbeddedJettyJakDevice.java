/**
@what Embedded Jetty Jakarta JAX-WS Device Simulator 

@note: See Eclipse Jetty: Programming Guide

*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jetty.server.Server;

import fence.util.ConfigurationData;

import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.soap.SOAPBinding;
import onvif_relay.service.JakDeviceImpl;

public class EmbeddedJettyJakDevice {
	
  public static void main(String[] args) throws Exception {
	System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
    ConfigurationData confData = new ConfigurationData(args);

    String srvPort = confData.getItem("onvif-device", "port");
	String port = confData.getItem("onvif-device", "device-port");
	String request = confData.getItem("onvif-device", "request");
	String ver = confData.getItem("onvif-device", "soap-ver");
	String level = confData.getItem("onvif-device", "log-level");
	
	String loglevel = "warning";
	if (level != null)
	  loglevel = level; 
	System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", loglevel);
	
    try {
        
      System.out.println("Starting the Jetty server on port: " + srvPort + " onvif mgt on: " + port);
        
      Server server = new Server(Integer.parseInt(srvPort));
      
      JakDeviceImpl device = new JakDeviceImpl();
      
      // System.setProperty("com.sun.net.httpserver.HttpServerProvider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      // System.setProperty("jakarta.xml.ws.spi.Provider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      // System.setProperty("javax.xml.ws.spi.Provider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
     
      String soapver = SOAPBinding.SOAP11HTTP_BINDING;
      if (ver.equals("12"))
    	soapver = SOAPBinding.SOAP12HTTP_BINDING;
      
      String uri = "http://127.0.0.1:" + port + request;
      Endpoint ep = Endpoint.create(soapver, device);
      ep.publish(uri);
      
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
