/**
 * @what A servlet to accept encapsulated ONVIF device requests and forward them.
 * 
 * @author John Hartley
 * 
 */

package onvif_relay.relay.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fence.util.ConfigurationData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.converters.OnvifOperations;
import onvif_relay.relay.invokers.InvokeOperation;

public class OnvifRelayServlet extends HttpServlet {
    
  private static final long serialVersionUID = 1L;
  // private final Collection<Map.Entry<String, String>> customHeaders;
  ConfigurationData conf = null;
  InvokeOperation onvifOp = new InvokeOperation();
  Gson ser = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
  Map<String, String> ctrl = new HashMap<>();
  
  public OnvifRelayServlet(ConfigurationData cd) {
	super();
	// this.customHeaders = new ArrayList<Map.Entry<String, String>>();
	conf = cd;
	ctrl.put("security", conf.getItem("onvif-defaults", "security"));
	ctrl.put("soap-ver", conf.getItem("onvif-default", "soap-ver"));
	ctrl.put("dump", conf.getItem("onvif-defaults", "dump"));
	ctrl.put("cxf", conf.getItem("onvif-defaults", "cxf"));
	ctrl.put("debug", conf.getItem("onvif-defaults", "debug"));
  }
   
  @Override
  public void init() throws ServletException {
    System.out.println("DBG>> OnvifRelayServlet:init");  
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		           throws IOException, ServletException {
    doPost(req, resp);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		           throws IOException, ServletException {  

	String reqURI = req.getRequestURI();
	String target = req.getParameter("uri");
	String operation = req.getParameter("operation");
	JsonRequestResponse jrro = new JsonRequestResponse();
	String res = "{}";
	
	try {
	  
	  if (target == null) {
	    Object[] op = OnvifOperations.getOperationPrototypes(operation);
	    
	    if (op != null) {
	      jrro.reqclass = op[0].getClass().getSimpleName();
	      jrro.respclass = op[1].getClass().getSimpleName();
	      jrro.request = op[0];
	      jrro.response = op[1];
		  
		  res = ser.toJson(jrro);
	    }
	  } else {
        URL targetUri = new URL(target);
        String auth = targetUri.getUserInfo();
        String proto = targetUri.getProtocol();
        String host = targetUri.getHost();
        int port = targetUri.getPort();
        String path = targetUri.getPath();
        String[] cred = auth.split(":");
        
        if (port != -1) {
          jrro.target = proto + "://" + host + ":" + Integer.toString(port) + path;
        } else {
          jrro.target = proto + "://" + host + path;
        }
        jrro.user = cred[0];
        jrro.password = cred[1];
        
        jrro.reqclass = operation;
        
        Object[] reqo = jrro.createRequest(operation, "{}");
        
        if (reqo != null) {
        
          jrro.request = reqo[0];
          jrro.response = reqo[1];
          
          jrro.response = onvifOp.invoke(jrro, true, ctrl);
          jrro.maskCredentials();
          
		  res = jrro.ser();
        }
	  }
	  /* if (null != this.customHeaders) {
	    for (Map.Entry<String, String> header : this.customHeaders) {
	      response.addHeader(header.getKey(), header.getValue());
	    }
	  } */
	} catch (Exception ex) {
	  throw new ServletException(ex);
	}
	
	resp.setContentType("application/json");
	resp.getOutputStream().println(res);
  }
  
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {  
    resp.setHeader("Allow", "GET, POST, OPTIONS");
  }

  public void addCustomHeader(final String key, final String value) {
    /*this.customHeaders.add(
	  new Map.Entry<String, String>() {
	  @Override
	  public String getKey() {
	    return key;
	  }
	  @Override
	  public String getValue() {
	    return value;
	  }
	  @Override
	  public String setValue(String value) {
	    return null;
	  }
	}); */
  }

  public void setCustomHeaders(Collection<Map.Entry<String, String>> headers) {
	/* this.customHeaders.clear();
	this.customHeaders.addAll(headers); */
  }
}
