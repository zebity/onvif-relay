package fence.jaxws;

import org.glassfish.jaxb.core.api.impl.NameConverter;
import java.lang.reflect.*;

// import org.glassfish.jaxb.core.api.impl.NameUtil;


public class MaintainUpperIdentifierNamesUtil implements NameConverter {
  static NameConverter standard = NameConverter.standard;
  final static NameConverter substitute = new MaintainUpperIdentifierNamesUtil();
  final static Class junk = forceInit(MaintainUpperIdentifierNamesUtil.class);
  
  public static <T> Class<T> forceInit(Class<T> forClass) {
	try {
	  Class.forName(forClass.getName(), true, forClass.getClassLoader());
	} catch (ClassNotFoundException ex) {
	  throw new AssertionError(ex);
	}
	return forClass;
  }
  
  MaintainUpperIdentifierNamesUtil() {
	  
    try {
	  Field field = NameConverter.class.getField("standard");
	  field.setAccessible(true);
	  
	  Field modifier = Field.class.getDeclaredField("modifiers");
	  modifier.setAccessible(true);
	  modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	  
	  field.set(null, (Object)substitute);
	  
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
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
      if (! Character.isUpperCase(token.charAt(0))) {
        res = standard.toVariableName(token);
      }
    }
    return(token);
  }

  @Override
  public String toPackageName(String namespaceUri) {
    return standard.toPackageName(namespaceUri);
  }
}
