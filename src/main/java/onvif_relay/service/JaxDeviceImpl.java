/**
 @what Test JAVAX onvif device service
 
 */
package onvif_relay.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.onvif.ver10.Device;
import org.onvif.ver10.device.wsdl.DeviceServiceCapabilities;
import org.onvif.ver10.device.wsdl.GetDot11Capabilities;
import org.onvif.ver10.device.wsdl.GetDot11CapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetEndpointReference;
import org.onvif.ver10.device.wsdl.GetEndpointReferenceResponse;
import org.onvif.ver10.device.wsdl.GetSystemUrisResponse.Extension;
import org.onvif.ver10.device.wsdl.Service;
import org.onvif.ver10.device.wsdl.StorageConfiguration;
import org.onvif.ver10.device.wsdl.StorageConfigurationData;
import org.onvif.ver10.schema.AttachmentData;
import org.onvif.ver10.schema.BackupFile;
import org.onvif.ver10.schema.BinaryData;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.CapabilityCategory;
import org.onvif.ver10.schema.Certificate;
import org.onvif.ver10.schema.CertificateInformation;
import org.onvif.ver10.schema.CertificateStatus;
import org.onvif.ver10.schema.CertificateWithPrivateKey;
import org.onvif.ver10.schema.DNSInformation;
import org.onvif.ver10.schema.DateTime;
import org.onvif.ver10.schema.DiscoveryMode;
import org.onvif.ver10.schema.Dot11AvailableNetworks;
import org.onvif.ver10.schema.Dot11Status;
import org.onvif.ver10.schema.Dot1XConfiguration;
import org.onvif.ver10.schema.DynamicDNSInformation;
import org.onvif.ver10.schema.DynamicDNSType;
import org.onvif.ver10.schema.FactoryDefaultType;
import org.onvif.ver10.schema.HostnameInformation;
import org.onvif.ver10.schema.IPAddress;
import org.onvif.ver10.schema.IPAddressFilter;
import org.onvif.ver10.schema.IntRange;
import org.onvif.ver10.schema.LocationEntity;
import org.onvif.ver10.schema.NTPInformation;
import org.onvif.ver10.schema.NetworkGateway;
import org.onvif.ver10.schema.NetworkHost;
import org.onvif.ver10.schema.NetworkInterface;
import org.onvif.ver10.schema.NetworkInterfaceSetConfiguration;
import org.onvif.ver10.schema.NetworkProtocol;
import org.onvif.ver10.schema.NetworkZeroConfiguration;
import org.onvif.ver10.schema.RelayLogicalState;
import org.onvif.ver10.schema.RelayOutput;
import org.onvif.ver10.schema.RelayOutputSettings;
import org.onvif.ver10.schema.RemoteUser;
import org.onvif.ver10.schema.Scope;
import org.onvif.ver10.schema.SetDateTimeType;
import org.onvif.ver10.schema.SupportInformation;
import org.onvif.ver10.schema.SystemDateTime;
import org.onvif.ver10.schema.SystemLog;
import org.onvif.ver10.schema.SystemLogType;
import org.onvif.ver10.schema.SystemLogUriList;
import org.onvif.ver10.schema.TimeZone;
import org.onvif.ver10.schema.User;

import javax.jws.WebService;
import javax.xml.ws.Holder;

@WebService(
  name = "Device",
  serviceName = "DeviceService",
  portName = "DevicePort",
  targetNamespace = "http://www.onvif.org/ver10")
public class JaxDeviceImpl implements Device {

	@Override
	public List<Service> getServices(boolean includeCapability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceServiceCapabilities getServiceCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getDeviceInformation(Holder<String> manufacturer, Holder<String> model, Holder<String> firmwareVersion,
			Holder<String> serialNumber, Holder<String> hardwareId) {
	  manufacturer.value = new String("john");
	  model.value = new String("beta");
	  firmwareVersion.value = new String("0.0.1");
	  serialNumber.value = new String("1");
	  hardwareId.value = new String("hw1");	
	}

	@Override
	public void setSystemDateAndTime(SetDateTimeType dateTimeType, boolean daylightSavings, TimeZone timeZone,
			DateTime utcDateTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SystemDateTime getSystemDateAndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSystemFactoryDefault(FactoryDefaultType factoryDefault) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String upgradeSystemFirmware(AttachmentData firmware) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String systemReboot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreSystem(List<BackupFile> backupFiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BackupFile> getSystemBackup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemLog getSystemLog(SystemLogType logType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupportInformation getSystemSupportInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Scope> getScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScopes(List<String> scopes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addScopes(List<String> scopeItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeScopes(Holder<List<String>> scopeItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DiscoveryMode getDiscoveryMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDiscoveryMode(DiscoveryMode discoveryMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DiscoveryMode getRemoteDiscoveryMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRemoteDiscoveryMode(DiscoveryMode remoteDiscoveryMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<NetworkHost> getDPAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetEndpointReferenceResponse getEndpointReference(GetEndpointReference parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteUser getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRemoteUser(RemoteUser remoteUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUsers(List<User> user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUsers(List<String> username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUser(List<User> user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWsdlUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getPasswordComplexityOptions(Holder<IntRange> minLenRange, Holder<IntRange> uppercaseRange,
			Holder<IntRange> numberRange, Holder<IntRange> specialCharsRange,
			Holder<Boolean> blockUsernameOccurrenceSupported, Holder<Boolean> policyConfigurationLockSupported) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPasswordComplexityConfiguration(Holder<Integer> minLen, Holder<Integer> uppercase,
			Holder<Integer> number, Holder<Integer> specialChars, Holder<Boolean> blockUsernameOccurrence,
			Holder<Boolean> policyConfigurationLocked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPasswordComplexityConfiguration(Integer minLen, Integer uppercase, Integer number,
			Integer specialChars, Boolean blockUsernameOccurrence, Boolean policyConfigurationLocked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPasswordHistoryConfiguration(Holder<Boolean> enabled, Holder<Integer> length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPasswordHistoryConfiguration(boolean enabled, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAuthFailureWarningOptions(Holder<IntRange> monitorPeriodRange, Holder<IntRange> authFailureRange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAuthFailureWarningConfiguration(Holder<Boolean> enabled, Holder<Integer> monitorPeriod,
			Holder<Integer> maxAuthFailures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuthFailureWarningConfiguration(boolean enabled, int monitorPeriod, int maxAuthFailures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Capabilities getCapabilities(List<CapabilityCategory> category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDPAddresses(List<NetworkHost> dpAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HostnameInformation getHostname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHostname(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setHostnameFromDHCP(boolean fromDHCP) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DNSInformation getDNS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDNS(boolean fromDHCP, List<String> searchDomain, List<IPAddress> dnsManual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NTPInformation getNTP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNTP(boolean fromDHCP, List<NetworkHost> ntpManual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DynamicDNSInformation getDynamicDNS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDynamicDNS(DynamicDNSType type, String name, Duration ttl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<NetworkInterface> getNetworkInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setNetworkInterfaces(String interfaceToken, NetworkInterfaceSetConfiguration networkInterface) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NetworkProtocol> getNetworkProtocols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetworkProtocols(List<NetworkProtocol> networkProtocols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkGateway getNetworkDefaultGateway() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetworkDefaultGateway(List<String> iPv4Address, List<String> iPv6Address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkZeroConfiguration getZeroConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setZeroConfiguration(String interfaceToken, boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPAddressFilter getIPAddressFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BinaryData getAccessPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAccessPolicy(BinaryData policyFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Certificate createCertificate(String certificateID, String subject, XMLGregorianCalendar validNotBefore,
			XMLGregorianCalendar validNotAfter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificate> getCertificates() {
	  List<Certificate> res = new ArrayList<>(); 
      return res;
	}

	@Override
	public List<CertificateStatus> getCertificatesStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCertificatesStatus(List<CertificateStatus> certificateStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCertificates(List<String> certificateID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BinaryData getPkcs10Request(String certificateID, String subject, BinaryData attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadCertificates(List<Certificate> nvtCertificate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getClientCertificateMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientCertificateMode(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Certificate> getCACertificates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadCertificateWithPrivateKey(List<CertificateWithPrivateKey> certificateWithPrivateKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CertificateInformation getCertificateInformation(String certificateID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadCACertificates(List<Certificate> caCertificate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDot1XConfiguration(Dot1XConfiguration dot1xConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDot1XConfiguration(Dot1XConfiguration dot1xConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dot1XConfiguration getDot1XConfiguration(String dot1xConfigurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dot1XConfiguration> getDot1XConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDot1XConfiguration(List<String> dot1xConfigurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetDot11CapabilitiesResponse getDot11Capabilities(GetDot11Capabilities parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dot11Status getDot11Status(String interfaceToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dot11AvailableNetworks> scanAvailableDot11Networks(String interfaceToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getSystemUris(Holder<SystemLogUriList> systemLogUris, Holder<String> supportInfoUri,
			Holder<String> systemBackupUri, Holder<Extension> extension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startFirmwareUpgrade(Holder<String> uploadUri, Holder<Duration> uploadDelay,
			Holder<Duration> expectedDownTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSystemRestore(Holder<String> uploadUri, Holder<Duration> expectedDownTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<StorageConfiguration> getStorageConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createStorageConfiguration(StorageConfigurationData storageConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageConfiguration getStorageConfiguration(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStorageConfiguration(StorageConfiguration storageConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteStorageConfiguration(String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LocationEntity> getGeoLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGeoLocation(List<LocationEntity> location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGeoLocation(List<LocationEntity> location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHashingAlgorithm(List<String> algorithm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RelayOutput> getRelayOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRelayOutputSettings(String relayOutputToken, RelayOutputSettings properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRelayOutputState(String relayOutputToken, RelayLogicalState logicalState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String sendAuxiliaryCommand(String auxiliaryCommand) {
		// TODO Auto-generated method stub
		return null;
	}

}
