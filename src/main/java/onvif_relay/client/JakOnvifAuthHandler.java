package onvif_relay.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

public class JakOnvifAuthHandler implements SOAPHandler<SOAPMessageContext> {
  boolean dumpSOAP = true;
  
  boolean setDumpSOAP(boolean to) {
    boolean res = dumpSOAP;
    dumpSOAP = to;
    return res;
  }
  
  void dumpSOAPMsg(SOAPMessage msg, OutputStream out) {
	ByteArrayOutputStream bufTo = new ByteArrayOutputStream();
	
	try {
	  msg.writeTo(bufTo);
	  ByteArrayInputStream bufFrom = new ByteArrayInputStream(bufTo.toByteArray());
	  
	  Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bufFrom);

	  Transformer tran = TransformerFactory.newInstance().newTransformer();
	  tran.setOutputProperty(OutputKeys.INDENT, "yes");
	
	  tran.transform(new DOMSource(doc), new StreamResult(out));
	  
	} catch (Exception ex) {
      ex.printStackTrace();
	}
  }
  
  @Override
  public boolean handleMessage(SOAPMessageContext cxt) {
    boolean res = true;


    System.out.println("DBG>> OnvifAuthHander::handleMessage - Out Bound(" +
    		             cxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) + ").");

    if ((Boolean)cxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) == true) {

      System.out.println("DBG>>  METHOD(" + cxt.get(MessageContext.HTTP_REQUEST_METHOD) + ") WSDL_OPERATION(" +
                         cxt.get(MessageContext.WSDL_OPERATION) + ") REQUEST_HEADERS(" +
          		         cxt.get(MessageContext.HTTP_REQUEST_HEADERS) + ").");

      Object hdrs = cxt.get(MessageContext.HTTP_REQUEST_HEADERS);
      
      System.out.println("DBG>> Request Headers: " + hdrs);
    } else {

      System.out.println("DBG>>  RESPONSE-CODE(" + cxt.get(MessageContext.HTTP_RESPONSE_CODE) + ") RESPONSE_HEADERS(" +
              		             cxt.get(MessageContext.HTTP_RESPONSE_HEADERS) + ").");
      
      if (dumpSOAP) {
    	System.out.println("DBG>> SOAP Message DUMP BEGIN >>");
        SOAPMessage msg = cxt.getMessage();
        dumpSOAPMsg(msg, System.out);
    	System.out.println("DBG>> SOAP Message DUMP END >>");
      }
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
