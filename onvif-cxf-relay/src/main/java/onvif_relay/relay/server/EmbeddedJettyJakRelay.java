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

    String srvPort = confData.getItem("onvif-relay", "port");
	String level = confData.getItem("onvif-relay", "log-level");
	String dump = confData.getItem("onvif-relay", "dump");
	String security = confData.getItem("onvif_relay, "security");
	String realm = confData.getItem("onvif-relay", "realm");
	String auth = confData.getItem("onvif-relay", "auth");
	String[] cred = auth.split(":");
	// ONVIF Roles: [ Administrator | Operator | User | Anonymous ]
	String[] roles = {"Administrator"};
	
	String loglevel = "warn";
	if (level != null)
	  loglevel = level; 
	System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", loglevel);
	
	if (dump != null && dump.equals("true")) {
	  // System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
	}
	
    try {
        
      System.out.println("Starting the Jetty server on port: " + srvPort + ".");
        
      Server server = new Server(Integer.parseInt(srvPort));
            
      ConnectHandler connect = new ConnectHandler();
      server.setHandler(connect);
      
      ServletContextHandler cxtHandler = new ServletContextHandler(proxy, "/", ServletContextHandler.SESSIONS);

      ConstraintSecurityHandler csh = setupAuth(cred, roles, realm, "/*");
      
      if (security.equals("basic")) {
        cxtHandler.setSecurityHandler(csh);
      }
      
      HttpServlet srvlet = new OnvRelayServlet(confData);
      ServletHolder holder = new ServletHolder(srvlet);
      cxtHandler.addServlet(holder, "/api/v1");
      
      server.start();
      
      // consoleLoop(DevManager);
      
      server.join();
      System.out.println("Stopped the simple server...");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  static ConstraintSecurityHandler setupAuth(String[] cred, String[] roles, String realm, String pathSpec ) {
	  
    HashLoginService loginSrv = new HashLoginService();
    loginSrv.setName(realm);
    
    UserStore creds = new UserStore();
    creds.addUser(cred[0], Credential.getCredential(cred[1]), roles);
    loginSrv.setUserStore(creds);
    
    Constraint secConstraint = new Constraint();
    secConstraint.setName(Constraint.__BASIC_AUTH);
    secConstraint.setRoles(roles);
    secConstraint.setAuthenticate(true);
      
    ConstraintMapping cm = new ConstraintMapping();
    cm.setConstraint(secConstraint);
    cm.setPathSpec(pathSpec);
      
    ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
    csh.setAuthenticator(new DigestAuthenticator());
    csh.addConstraintMapping(cm);
    csh.setLoginService(loginSrv);
    
    return csh;
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
