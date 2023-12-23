package onvif_relay.relay.invokers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class UTPasswordCallback implements CallbackHandler {
  static Map<String, String> passwords = new HashMap<>();
  
 public UTPasswordCallback() {
 }
 
 public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
   for (int i = 0; i < callbacks.length; i++) {
	 WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
	 
	 String pass = passwords.get(pc.getIdentifier());
	 if (pass != null) {
	   pc.setPassword(pass);
	   break;
	 }
   }
 }
 
 public static void setAliasPassword(String alias, String password) {
    passwords.put(alias, password);
 }
}