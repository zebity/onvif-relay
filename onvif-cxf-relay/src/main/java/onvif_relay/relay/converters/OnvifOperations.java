package onvif_relay.relay.converters;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OnvifOperations {
  /* GENERATED by XSLT */ static String[][] DeviceOperations = {
	    {"GetServices", "org.onvif.ver10.device.wsdl"},
	    {"GetServiceCapabilities", "org.onvif.ver10.device.wsdl"},
	    {"GetDeviceInformation", "org.onvif.ver10.device.wsdl"},
	    {"SetSystemDateAndTime", "org.onvif.ver10.device.wsdl"},
	    {"GetSystemDateAndTime", "org.onvif.ver10.device.wsdl"},
	    {"SetSystemFactoryDefault", "org.onvif.ver10.device.wsdl"},
	    {"UpgradeSystemFirmware", "org.onvif.ver10.device.wsdl"},
	    {"SystemReboot", "org.onvif.ver10.device.wsdl"},
	    {"RestoreSystem", "org.onvif.ver10.device.wsdl"},
	    {"GetSystemBackup", "org.onvif.ver10.device.wsdl"},
	    {"GetSystemLog", "org.onvif.ver10.device.wsdl"},
	    {"GetSystemSupportInformation", "org.onvif.ver10.device.wsdl"},
	    {"GetScopes", "org.onvif.ver10.device.wsdl"},
	    {"SetScopes", "org.onvif.ver10.device.wsdl"},
	    {"AddScopes", "org.onvif.ver10.device.wsdl"},
	    {"RemoveScopes", "org.onvif.ver10.device.wsdl"},
	    {"GetDiscoveryMode", "org.onvif.ver10.device.wsdl"},
	    {"SetDiscoveryMode", "org.onvif.ver10.device.wsdl"},
	    {"GetRemoteDiscoveryMode", "org.onvif.ver10.device.wsdl"},
	    {"SetRemoteDiscoveryMode", "org.onvif.ver10.device.wsdl"},
	    {"GetDPAddresses", "org.onvif.ver10.device.wsdl"},
	    {"SetDPAddresses", "org.onvif.ver10.device.wsdl"},
	    {"GetEndpointReference", "org.onvif.ver10.device.wsdl"},
	    {"GetRemoteUser", "org.onvif.ver10.device.wsdl"},
	    {"SetRemoteUser", "org.onvif.ver10.device.wsdl"},
	    {"GetUsers", "org.onvif.ver10.device.wsdl"},
	    {"CreateUsers", "org.onvif.ver10.device.wsdl"},
	    {"DeleteUsers", "org.onvif.ver10.device.wsdl"},
	    {"SetUser", "org.onvif.ver10.device.wsdl"},
	    {"GetWsdlUrl", "org.onvif.ver10.device.wsdl"},
	    {"GetPasswordComplexityOptions", "org.onvif.ver10.device.wsdl"},
	    {"GetPasswordComplexityConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetPasswordComplexityConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetPasswordHistoryConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetPasswordHistoryConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetAuthFailureWarningOptions", "org.onvif.ver10.device.wsdl"},
	    {"GetAuthFailureWarningConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetAuthFailureWarningConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetCapabilities", "org.onvif.ver10.device.wsdl"},
	    {"GetHostname", "org.onvif.ver10.device.wsdl"},
	    {"SetHostname", "org.onvif.ver10.device.wsdl"},
	    {"SetHostnameFromDHCP", "org.onvif.ver10.device.wsdl"},
	    {"GetDNS", "org.onvif.ver10.device.wsdl"},
	    {"SetDNS", "org.onvif.ver10.device.wsdl"},
	    {"GetNTP", "org.onvif.ver10.device.wsdl"},
	    {"SetNTP", "org.onvif.ver10.device.wsdl"},
	    {"GetDynamicDNS", "org.onvif.ver10.device.wsdl"},
	    {"SetDynamicDNS", "org.onvif.ver10.device.wsdl"},
	    {"GetNetworkInterfaces", "org.onvif.ver10.device.wsdl"},
	    {"SetNetworkInterfaces", "org.onvif.ver10.device.wsdl"},
	    {"GetNetworkProtocols", "org.onvif.ver10.device.wsdl"},
	    {"SetNetworkProtocols", "org.onvif.ver10.device.wsdl"},
	    {"GetNetworkDefaultGateway", "org.onvif.ver10.device.wsdl"},
	    {"SetNetworkDefaultGateway", "org.onvif.ver10.device.wsdl"},
	    {"GetZeroConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetZeroConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetIPAddressFilter", "org.onvif.ver10.device.wsdl"},
	    {"SetIPAddressFilter", "org.onvif.ver10.device.wsdl"},
	    {"AddIPAddressFilter", "org.onvif.ver10.device.wsdl"},
	    {"RemoveIPAddressFilter", "org.onvif.ver10.device.wsdl"},
	    {"GetAccessPolicy", "org.onvif.ver10.device.wsdl"},
	    {"SetAccessPolicy", "org.onvif.ver10.device.wsdl"},
	    {"GetRelayOutputs", "org.onvif.ver10.device.wsdl"},
	    {"SetRelayOutputSettings", "org.onvif.ver10.device.wsdl"},
	    {"SetRelayOutputState", "org.onvif.ver10.device.wsdl"},
	    {"SendAuxiliaryCommand", "org.onvif.ver10.device.wsdl"},
	    {"GetDot11Capabilities", "org.onvif.ver10.device.wsdl"},
	    {"GetDot11Status", "org.onvif.ver10.device.wsdl"},
	    {"ScanAvailableDot11Networks", "org.onvif.ver10.device.wsdl"},
	    {"GetSystemUris", "org.onvif.ver10.device.wsdl"},
	    {"StartFirmwareUpgrade", "org.onvif.ver10.device.wsdl"},
	    {"StartSystemRestore", "org.onvif.ver10.device.wsdl"},
	    {"GetStorageConfigurations", "org.onvif.ver10.device.wsdl"},
	    {"CreateStorageConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetStorageConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetStorageConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"DeleteStorageConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetGeoLocation", "org.onvif.ver10.device.wsdl"},
	    {"SetGeoLocation", "org.onvif.ver10.device.wsdl"},
	    {"DeleteGeoLocation", "org.onvif.ver10.device.wsdl"},
	    {"SetHashingAlgorithm", "org.onvif.ver10.device.wsdl"},
	    {"CreateCertificate", "org.onvif.ver10.device.wsdl"},
	    {"GetCertificates", "org.onvif.ver10.device.wsdl"},
	    {"GetCertificatesStatus", "org.onvif.ver10.device.wsdl"},
	    {"SetCertificatesStatus", "org.onvif.ver10.device.wsdl"},
	    {"DeleteCertificates", "org.onvif.ver10.device.wsdl"},
	    {"GetPkcs10Request", "org.onvif.ver10.device.wsdl"},
	    {"LoadCertificates", "org.onvif.ver10.device.wsdl"},
	    {"GetClientCertificateMode", "org.onvif.ver10.device.wsdl"},
	    {"SetClientCertificateMode", "org.onvif.ver10.device.wsdl"},
	    {"GetCACertificates", "org.onvif.ver10.device.wsdl"},
	    {"LoadCertificateWithPrivateKey", "org.onvif.ver10.device.wsdl"},
	    {"GetCertificateInformation", "org.onvif.ver10.device.wsdl"},
	    {"LoadCACertificates", "org.onvif.ver10.device.wsdl"},
	    {"CreateDot1XConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"SetDot1XConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetDot1XConfiguration", "org.onvif.ver10.device.wsdl"},
	    {"GetDot1XConfigurations", "org.onvif.ver10.device.wsdl"},
	    {"DeleteDot1XConfiguration", "org.onvif.ver10.device.wsdl"}
	    };
  /* GENERATED by XSLT */ static String[][] MediaOperations = {
	    {"GetServiceCapabilities", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoSources", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioSources", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioOutputs", "org.onvif.ver10.media.wsdl"},
	    {"CreateProfile", "org.onvif.ver10.media.wsdl"},
	    {"GetProfile", "org.onvif.ver10.media.wsdl"},
	    {"GetProfiles", "org.onvif.ver10.media.wsdl"},
	    {"AddVideoEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveVideoEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddVideoSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveVideoSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddAudioEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveAudioEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddAudioSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveAudioSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddPTZConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemovePTZConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddVideoAnalyticsConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveVideoAnalyticsConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddMetadataConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveMetadataConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddAudioOutputConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveAudioOutputConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"AddAudioDecoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"RemoveAudioDecoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"DeleteProfile", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoSourceConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoEncoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioSourceConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioEncoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoAnalyticsConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetMetadataConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioOutputConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioDecoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoAnalyticsConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetMetadataConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioOutputConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioDecoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleVideoEncoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleVideoSourceConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleAudioEncoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleAudioSourceConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleVideoAnalyticsConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleMetadataConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleAudioOutputConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"GetCompatibleAudioDecoderConfigurations", "org.onvif.ver10.media.wsdl"},
	    {"SetVideoSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetVideoEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetAudioSourceConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetAudioEncoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetVideoAnalyticsConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetMetadataConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetAudioOutputConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"SetAudioDecoderConfiguration", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoSourceConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoEncoderConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioSourceConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioEncoderConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetMetadataConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioOutputConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetAudioDecoderConfigurationOptions", "org.onvif.ver10.media.wsdl"},
	    {"GetGuaranteedNumberOfVideoEncoderInstances", "org.onvif.ver10.media.wsdl"},
	    {"GetStreamUri", "org.onvif.ver10.media.wsdl"},
	    {"StartMulticastStreaming", "org.onvif.ver10.media.wsdl"},
	    {"StopMulticastStreaming", "org.onvif.ver10.media.wsdl"},
	    {"SetSynchronizationPoint", "org.onvif.ver10.media.wsdl"},
	    {"GetSnapshotUri", "org.onvif.ver10.media.wsdl"},
	    {"GetVideoSourceModes", "org.onvif.ver10.media.wsdl"},
	    {"SetVideoSourceMode", "org.onvif.ver10.media.wsdl"},
	    {"GetOSDs", "org.onvif.ver10.media.wsdl"},
	    {"GetOSD", "org.onvif.ver10.media.wsdl"},
	    {"GetOSDOptions", "org.onvif.ver10.media.wsdl"},
	    {"SetOSD", "org.onvif.ver10.media.wsdl"},
	    {"CreateOSD", "org.onvif.ver10.media.wsdl"},
	    {"DeleteOSD", "org.onvif.ver10.media.wsdl"}
	    };
  static String[][]  Classifiers = {
		  {"get", "get"},
		  {"set", "set"},
		  {"add", "update"},
		  {"create", "update"},
		  {"delete", "update"},
		  {"remove", "update"},
		  {"", "action"}
         };

  static org.onvif.ver10.device.wsdl.ObjectFactory dof = new org.onvif.ver10.device.wsdl.ObjectFactory();
  static org.onvif.ver10.media.wsdl.ObjectFactory mof = new org.onvif.ver10.media.wsdl.ObjectFactory();

  public static final String DeviceType = "org.onvif.ver10.device.wsdl";
  public static final String MediaType = "org.onvif.ver10.media.wsdl";
  public static OnvifSchema schema = OnvifSchema.createTop();
  static Class<?>[] emptyArgs = {};
  static Object[] emptyParams = {};
  public static boolean doneInit = init();
  
  static boolean init() {
    boolean res = true;
    
    // deviceOps = new HashMap<>();
    // mediaOps = new HashMap<>();
    // (int i = 0; i < DeviceOperations.length; i++)
    //  deviceOps.put(DeviceOperations[i][0], DeviceOperations[i]);
    
    // for (int i = 0; i < MediaOperations.length; i++)
    //  mediaOps.put(MediaOperations[i][0], MediaOperations[i]);
    
    // factories = new HashMap<>();
    // factories.put("org.onvif.ver10.device.wsdl", dof);
    // factories.put("org.onvif.ver10.media.wsdl", mof);
    
    // schema = OnvifSchema.createTop();
    OnvifSchema stab = schema.addVersion("ver10");
    stab.addOperations("device", DeviceType, dof, DeviceOperations);
    stab.addOperations("media", MediaType, mof, MediaOperations);
    return res;
  }
  
  public static String getOperationNamespace(String forReq, String ver) {
	String res = null;
	OnvifSchema fnd = schema.findOperation(forReq, ver);
	
	if (fnd != null) {
     res = fnd.namespace;
	}
	return res;
  }
  
  public static Object[] getOperationPrototypes(String forReq, String ver) {
	Object[] res = null;
	
	OnvifSchema use = schema.findOperation(forReq, ver);
	
	// String[] use = deviceOps.get(forReq);
	// if (use == null) {
    //   use = mediaOps.get(forReq);
	// }
	
	if (use != null) {
	  Object fact = use.factory;
	  
	  try {
	  
	    Method reqCreate = fact.getClass().getDeclaredMethod(new String("create" + forReq), emptyArgs);
	    Method respCreate = fact.getClass().getDeclaredMethod(new String("create" + forReq + "Response"), emptyArgs);
	    
	    Object reqo = reqCreate.invoke(fact, emptyParams);
	    Object respo = respCreate.invoke(fact, emptyParams);
	    
	    if (reqo != null || respo != null) {
	      res = new Object[3];
	      res[0] = reqo;
	      res[1] = respo;
	      res[2] = use.namespace;
	    }
	  } catch (Exception ex) {
	    ex.printStackTrace();
	  }
	}
	return res;
  }
}
