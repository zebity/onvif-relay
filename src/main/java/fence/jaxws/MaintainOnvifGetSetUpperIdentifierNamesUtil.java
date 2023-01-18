package fence.jaxws;

import org.glassfish.jaxb.core.api.impl.NameConverter;
import java.lang.reflect.*;

// import org.glassfish.jaxb.core.api.impl.NameUtil;


public class MaintainOnvifGetSetUpperIdentifierNamesUtil implements NameConverter {
  final static NameConverter standard;
  final static NameConverter substitute;
  
  static {

	try {
	  
	  Standard sync = new Standard();
	  
	  standard = NameConverter.standard;
	  Class.forName(MaintainOnvifGetSetUpperIdentifierNamesUtil.class.getName(), true, MaintainOnvifGetSetUpperIdentifierNamesUtil.class.getClassLoader());
	  substitute = MaintainOnvifGetSetUpperIdentifierNamesUtil.class.getDeclaredConstructor().newInstance();
	  
	  try {
	    Field field = NameConverter.class.getField("standard");
	    field.setAccessible(true);
	    Field modifier = Field.class.getDeclaredField("modifiers");
	    modifier.setAccessible(true);
	    modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	    field.set(null, (Object)substitute);

        System.out.println("INFO>> MaintainOnvifGetSetUpperIdentifierNamesUtil Enabled!!");            
	  } catch (Exception secEx) {
		System.out.println(secEx.getMessage());
	  }
	  
	} catch (ClassNotFoundException ex) {
		
	  throw new AssertionError(ex);
	  
	} catch (Exception ex) {
		
	  throw new AssertionError(ex);
	  
	}
  }
  
  public MaintainOnvifGetSetUpperIdentifierNamesUtil() {
  }

  @Override
  public String toClassName(String token) {
	return standard.toClassName(token);
  }

  @Override
  public String toInterfaceName(String token) {
    return standard.toInterfaceName(token);
  }

  @Override
  public String toPropertyName(String token) {
    return standard.toPropertyName(token);
  }

  @Override
  public String toConstantName(String token) {
    return standard.toConstantName(token);
  }

  @Override
  public String toVariableName(String token) {
	String res = token;
    if (token != null) {
      if (token.length() > 3 && (token.startsWith("Get") || token.startsWith("Set"))) {
    	System.out.println("INFO>> MaintainOnvifGetSetUpperNamesUtil::toVariable: " + token);
      } else {
        res = standard.toVariableName(token);
      }
    }
    return(res);
  }

  @Override
  public String toPackageName(String namespaceUri) {
    return standard.toPackageName(namespaceUri);
  }
}
