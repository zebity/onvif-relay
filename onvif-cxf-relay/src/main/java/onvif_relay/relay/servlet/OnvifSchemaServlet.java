/**
 * @what A servlet to accepts ONVIF schema lookup requests and returns prototype JSON object.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;


import fence.util.ConfigurationData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import onvif_relay.relay.converters.JsonRequestResponse;
import onvif_relay.relay.converters.OnvifOperations;
import onvif_relay.relay.converters.OnvifSchema;
import onvif_relay.relay.invokers.InvokeOperation;

public class OnvifSchemaServlet extends HttpServlet {
    
  private static final long serialVersionUID = 1L;
  ConfigurationData conf = null;
  InvokeOperation onvifOp = new InvokeOperation();
  ObjectMapper ser = new ObjectMapper()
		              .enable(SerializationFeature.INDENT_OUTPUT)
		              .registerModule(new JakartaXmlBindAnnotationModule());
  Map<String, String> ctrl = new HashMap<>();
  
  public OnvifSchemaServlet(ConfigurationData cd) {
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
    boolean check = OnvifOperations.doneInit;
    System.out.println("DBG>> OnvifSchemaServlet:init - " + check);
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		           throws IOException, ServletException {
    doPost(req, resp);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		           throws IOException, ServletException {
  // |/api/schema|/verxx/operation

	String reqURI = req.getRequestURI();
	// String target = req.getParameter("uri");
	String path = req.getPathInfo();
	String[] pbits = path.split("/");
	String version = null;
	String operation = null;
	JsonRequestResponse jrro = new JsonRequestResponse();
	String res = "{}";
	
	try {
	  
	  if (pbits.length == 3) {
		version = pbits[1];
		operation = pbits[2];
		
	    Object[] op = OnvifOperations.getOperationPrototypes(operation, version);
	    
	    if (op != null) {
	      jrro.reqclass = op[0].getClass().getSimpleName();
	      jrro.respclass = op[1].getClass().getSimpleName();
	      jrro.request = op[0];
	      jrro.response = op[1];
		  
		  res = jrro.ser();
        }
	  } else if (pbits.length == 2) {
		version = pbits[1];
        OnvifSchema onvs = OnvifOperations.schema.findVersion(version);
        
        ObjectMapper om = new ObjectMapper();
        
        res = om.writeValueAsString(onvs);
		  
	  }

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
