/**
@what Embedded Jetty Jakarta JAX-WS Device Simulator 

@note: See Eclipse Jetty: Programming Guide

*/

package onvif_relay.relay.server;

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
import onvif_relay.relay.servlet.OnvifRelayServlet;
import onvif_relay.relay.servlet.OnvifSchemaServlet;

public class EmbeddedJettyJakRelay {
	
  public static void main(String[] args) throws Exception {
	System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
    ConfigurationData conf = new ConfigurationData(args);

    String srvPort = conf.getItem("onvif-relay", "port");
	String level = conf.getItem("onvif-relay", "log-level");
	String dump = conf.getItem("onvif-relay", "dump");
	String security = conf.getItem("onvif-relay", "security");
	String realm = conf.getItem("onvif-relay", "realm");
	String auth = conf.getItem("onvif-relay", "auth");
	String relay = conf.getItem("onvif-relay", "relay");
	String schema = conf.getItem("onvif-relay", "schema");
	String relaypath = conf.getItem("onvif-relay", "path-relay");
	String schemapath = conf.getItem("onvif-relay", "path-schema");
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
      
      ServletContextHandler cxtHandler = new ServletContextHandler(connect, "/", ServletContextHandler.SESSIONS);

      ConstraintSecurityHandler csh = setupAuth(cred, roles, realm, "/*");
      
      if (security.equals("basic")) {
        cxtHandler.setSecurityHandler(csh);
      }
      
      HttpServlet rsrvlet = null, ssrvlet = null;
      ServletHolder rholder=null, sholder = null;
      if (relay != null && relay.equals("true")) {
        rsrvlet = new OnvifRelayServlet(conf);
        rholder = new ServletHolder(rsrvlet);
        cxtHandler.addServlet(rholder, relaypath);
      }
      if (schema != null && schema.equals("true")) {
          ssrvlet = new OnvifSchemaServlet(conf);
          sholder = new ServletHolder(ssrvlet);
          cxtHandler.addServlet(sholder, schemapath);
      }
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
