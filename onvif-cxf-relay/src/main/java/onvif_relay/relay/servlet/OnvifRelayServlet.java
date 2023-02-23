/**
 * @what A servlet to accept encapsulated ONVIF device requests and forward them.
 * 
 * @author John Hartley
 * 
 */

package onvif_relay.relay.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import fence.util.ConfigurationData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnvifRelayServlet extends HttpServlet {
    
  private static final long serialVersionUID = 1L;
  private final Collection<Map.Entry<String, String>> customHeaders;
  ConfigurationData confData = null;
  
  public OnvifRelayServlet(ConfigurationData cfd) {
	super();
	this.customHeaders = new ArrayList<Map.Entry<String, String>>();
	confData = cfd;
  }
   
  @Override
  public void init() throws ServletException {
    System.out.println("DBG>> OnvifRelayServlet:init");  
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {  

	try {
	  response.setContentType("application/x-onvif");

	  if (null != this.customHeaders) {
	    for (Map.Entry<String, String> header : this.customHeaders) {
	      response.addHeader(header.getKey(), header.getValue());
	    }
	  }


	} catch (Exception ex) {
	  throw new ServletException(ex);
	}
  }
  
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {  
    resp.setHeader("Allow", "GET, POST, OPTIONS");
  }

  public void addCustomHeader(final String key, final String value) {
    this.customHeaders.add(
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
	  });
  }

  public void setCustomHeaders(Collection<Map.Entry<String, String>> headers) {
	this.customHeaders.clear();
	this.customHeaders.addAll(headers);
  }
}
