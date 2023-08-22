package onvif_relay.relay.converters;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class OnvifSchema {
  String version = null,
	     type = null,
		 namespace = null,
		 operation = null;
  Object factory = null;
  Map<String, OnvifSchema> versions = null;
  Map<String, OnvifSchema> types = null;
  Map<String, String[]> operations = null;
  // static boolean initops = OnvifOperations.doneInit;
  
  public OnvifSchema(String ver) {
	version = ver;
	types = new LinkedHashMap<>();
  }
  public OnvifSchema(String ver, String t, String ns, Object f, String[][] ops) {
    version = ver;
    type = t;
    namespace = ns;
    factory = f;
    operations = new HashMap<>();
    for (String[] o: ops) {
      operations.put(o[0],  o);
    }		
  }
  public OnvifSchema(OnvifSchema os, String op) {
    version = os.version;
    type = os.type;
    namespace = os.namespace;
    factory = os.factory;
    operation = op;
  }
  OnvifSchema() {
  }
  static OnvifSchema createTop() {
	OnvifSchema res = new OnvifSchema();     
	res.versions = new LinkedHashMap<>();
	return res;
  }
  OnvifSchema addVersion(String ver) {
    OnvifSchema res = new OnvifSchema(ver);
    versions.put(ver, res);
    return res;
  }
  OnvifSchema addOperations(String t, String ns, Object f, String[][] ops) {
    OnvifSchema res = new OnvifSchema(version, t, ns, f, ops);
    types.put(t, res);
    return res;
  }
  
  private OnvifSchema findOperation(String op) {
	OnvifSchema res = null;
	String [] fnd = null;
      
	for (OnvifSchema dtree: types.values()) {
	  fnd = dtree.operations.get(op);
	  if (fnd != null) {
	    res = new OnvifSchema(dtree, op);
	    break;
	  }
	}	
	return res;
  }
  
  public OnvifSchema findOperation(String op, String ver) {
    OnvifSchema res = null;
    if (ver ==  null) {
      
      for (OnvifSchema stree: versions.values()) {
    	res = stree.findOperation(op);
    	if (res != null)
    	  break;
      }
    } else {
      OnvifSchema stree = versions.get(ver);
      if (stree != null) {
        res = stree.findOperation(op);
      }
    }
	
    return res;
  }
  
  public OnvifSchema findVersion(String ver) {
	OnvifSchema res = versions.get(ver);
	return res;
  }
}
