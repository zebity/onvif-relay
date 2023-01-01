package fence.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationData {
  String defaultConfig = "/usr/local/etc/default.ini";
  String configFile = null;
  Map<String, Map<String, String>> confMap = new HashMap<>();

  
  public ConfigurationData(String[] argv) {
    Map<String, String> flags = new HashMap<>();
    List<String> argList = new ArrayList<>();
    
    parseArgs(argv, flags, argList);
    
    String confFile = flags.get("config");
    if (confFile != null) {
      loadConfig(confFile);
    } else {
      confFile = getConfigEnv();
      if (confFile != null) {
    	loadConfig(confFile);
      } else {
    	loadConfig(defaultConfig);
      }
    }
  }
  String getConfigEnv() {
    String res = null;
    
    res = System.getenv("DEFAULT_CONFIG");
    
    return res;
  }
  
  int loadConfig(String from) {
	int res = 0;
	
    configFile = from;
    
    INIConfiguration ini = new INIConfiguration();
    
    try {
      BufferedReader fromFile = new BufferedReader(new FileReader(configFile));
      ini.read(fromFile);
      
      Set<String> configSections = ini.getSections();
      
      for (String secName: configSections) {
        SubnodeConfiguration configSection = ini.getSection(secName);
        
        Map<String, String> secMap = new HashMap<>();
        
        confMap.put(secName, secMap);
        if (configSection != null) {
          Iterator<String> keys = configSection.getKeys();
          
          while (keys.hasNext()) {
            String key = keys.next();
            String value = configSection.getString(key);
            
            if (value != null) {
              secMap.put(key, value);
            }
          }
        }
      }
    } catch (ConfigurationException  ex) {
      System.out.println(ex);
    } catch (IOException ex) {
      System.out.println(ex);	
    }
    return res;
  }
  
  int parseArgs(String[] args, Map<String, String> flags, List<String> sf) {
	int res = 0;

	for (int i = 0; i < args.length; i++) {
      String a = args[i];
	  if (a.charAt(0) == '-' && a.length() > 1) {
	    switch(a.charAt(1)) {
	      case 'c': if (i+1 > args.length) {
	  		          System.out.println("Invalid argument: [" + a + "].");
	  		          res--;
	                }
	                else {
	    	          flags.put("config", args[i+1]);
	    	          i++; /* skip past dirpath */
	                }
	                break;
	      default: System.out.println("Invalid argument: [" + a + "].");
	               res--;
	               break;
	    }
	  }
	  else if (a.length() > 1) {
        sf.add(a);
	  }
	  else {
		System.out.println("Invalid argument: [" + a + "].");
		res--;
	  }
	}
	return res;
  }
  
  public String getItem(String section, String name) {
    String res = null;
    
    Map<String, String> secMap = confMap.get(section);
    
    if (secMap != null) {
      res = secMap.get(name);
    }
    return res;
  }
}
