/**
@what Embedded Jetty Jakarta JAX-WS Device Simulator 

@note: See Eclipse Jetty: Programming Guide

*/

package onvif_relay.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

import fence.util.ConfigurationData;
import jakarta.servlet.http.HttpServlet;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.soap.SOAPBinding;
import onvif_relay.service.JakDeviceImpl;
import onvif_relay.service.JakMediaImpl;
import onvif_relay.servlet.OnvifFacadeServlet;

public class EmbeddedJettyJakDevice {
	
  public static void main(String[] args) throws Exception {
	System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
    ConfigurationData confData = new ConfigurationData(args);

    String srvPort = confData.getItem("onvif-device", "port");
	String dport = confData.getItem("onvif-device", "device-port");
	String mport = confData.getItem("onvif-device", "media-port");
	String devrequest = confData.getItem("onvif-device", "device-service");
	String medrequest = confData.getItem("onvif-device", "media-service");
	String ver = confData.getItem("onvif-device", "soap-ver");
	String level = confData.getItem("onvif-device", "log-level");
	String dump = confData.getItem("onvif-device", "dump");
	String security = confData.getItem("onvif-device", "security");
	String realm = confData.getItem("onvif-device", "realm");
	String auth = confData.getItem("onvif-device", "auth");
	String[] cred = auth.split(":");
	// ONVIF Roles: [ Administrator | Operator | User | Anonymous ]
	String[] roles = {"Administrator"};
	
	String loglevel = "warn";
	if (level != null)
	  loglevel = level; 
	System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", loglevel);
	
	if (dump != null && (dump.equals("true") || dump.equals("yes"))) {
	  System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
	}
	
    try {
        
      System.out.println("Starting the Jetty server on port: " + srvPort + " onvif[" + dport + "," + mport + "].");
        
      Server server = new Server(Integer.parseInt(srvPort));
      
      JakDeviceImpl device = new JakDeviceImpl();
      JakMediaImpl media = new JakMediaImpl();     
      
      // System.setProperty("com.sun.net.httpserver.HttpServerProvider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      // System.setProperty("jakarta.xml.ws.spi.Provider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
      System.setProperty("jakarta.xml.ws.spi.Provider", "org.apache.cxf.jaxws.spi.ProviderImpl");
      // System.setProperty("javax.xml.ws.spi.Provider", "org.eclipse.jetty.http.spi.JettyHttpServerProvider");
     
      String soapver = SOAPBinding.SOAP11HTTP_BINDING;
      if (ver.equals("12"))
    	soapver = SOAPBinding.SOAP12HTTP_BINDING;
      
      String devuri = "http://127.0.0.1:" + dport + devrequest;
      Endpoint devep = null;
      if (ver.equals("default")) {
          devep = Endpoint.create(device); 
      } else {
        devep = Endpoint.create(soapver, device);
      }
      if (devep != null) {
        devep.publish(devuri);
      } else {
        System.out.println("ERR>> EmbeddedJettyJakDevice::main - Failed to create Device EndPoint."); 
      }
      
      String mediauri = "http://127.0.0.1:" + mport + medrequest;
      Endpoint mediaep = null;
      if (ver.equals("default")) {
        mediaep = Endpoint.create(media);
      } else {
          mediaep = Endpoint.create(soapver, media);  
      }
      if (mediaep != null) {
        mediaep.publish(mediauri);
      } else {
        System.out.println("ERR>> EmbeddedJettyJakDevice::main - Failed to create Media EndPoint."); 
      }
      
      ConnectHandler proxy = new ConnectHandler();
      server.setHandler(proxy);
      
      HashLoginService loginSrv = new HashLoginService();
      loginSrv.setName(realm);
      UserStore creds = new UserStore();
      creds.addUser(cred[0], Credential.getCredential(cred[1]), roles);
      loginSrv.setUserStore(creds);
      Constraint secConstraint = new Constraint();
      secConstraint.setName(Constraint.__DIGEST_AUTH);
      secConstraint.setRoles(roles);
      secConstraint.setAuthenticate(true);
      
      ConstraintMapping cm = new ConstraintMapping();
      cm.setConstraint(secConstraint);
      cm.setPathSpec("/*");
      
      ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
      csh.setAuthenticator(new DigestAuthenticator());
      csh.addConstraintMapping(cm);
      csh.setLoginService(loginSrv);
      // ServletHandler srvHandler = new ServletHandler();
      
      ServletContextHandler cxtHandler = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);

      if (security.equals("digest")) {
        cxtHandler.setSecurityHandler(csh);
      }
      
      HttpServlet srvlet = new OnvifFacadeServlet(confData);
      ServletHolder holder = new ServletHolder(srvlet);
      // srvHandler.addServletWithMapping(holder, "/onvif/device_service");
      cxtHandler.addServlet(holder, "/onvif/device_service");
      // server.setHandler(srvHandler);
      
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
