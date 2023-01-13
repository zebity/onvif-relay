package onvif_relay.client;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.namespace.QName;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class JaxOnvifAuthHandler implements SOAPHandler<SOAPMessageContext> {
  
  @Override
  public boolean handleMessage(SOAPMessageContext cxt) {
    boolean res = true;


    System.out.println("DBG>> OnvifAuthHander::handleMessage - Out Bound(" +
    		             cxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) + ").");

    if ((Boolean)cxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) == true) {

      System.out.println("DBG>>  METHOD(" + cxt.get(MessageContext.HTTP_REQUEST_METHOD) + ") REQUEST_HEADERS(" +
          		             cxt.get(MessageContext.HTTP_REQUEST_HEADERS) + ").");

      Object hdrs = cxt.get(MessageContext.HTTP_REQUEST_HEADERS);
      
      System.out.println("DBG>> Request Headers: " + hdrs);
    } else {

      System.out.println("DBG>>  RESPONSE-CODE(" + cxt.get(MessageContext.HTTP_RESPONSE_CODE) + ") RESPONSE_HEADERS(" +
              		             cxt.get(MessageContext.HTTP_RESPONSE_HEADERS) + ").");
    }
	return res;
  }

  @Override
  public boolean handleFault(SOAPMessageContext cxt) {
	boolean res = true;

    System.out.println("DBG>> OnvifDeviceAuthHander::handleFault - Out Bound(" +
      		             cxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) +
      		             ") Resonse Code(" +
      		             cxt.get(MessageContext.HTTP_RESPONSE_CODE + ")."));
	return res;
  }

  @Override
  public void close(MessageContext context) {
  	// TODO Auto-generated method stub
  	
  }
  
  @Override
  public Set<QName> getHeaders() {
	// TODO Auto-generated method stub
	return null;
  }
  @PostConstruct
  public void init() {
		
  }
	
  @PreDestroy
    public void cleanup() {
		
  }

}
