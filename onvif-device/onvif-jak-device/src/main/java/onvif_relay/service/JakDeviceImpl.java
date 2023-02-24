/**
 @what Test JAVAX onvif device service
 
 */

package onvif_relay.service;

import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.device.wsdl.DeviceServiceCapabilities;
import org.onvif.ver10.device.wsdl.GetDot11CapabilitiesResponse;
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

import jakarta.jws.WebService;
import jakarta.xml.ws.Holder;

@WebService(
  name = "Device",
  serviceName = "DeviceService",
  portName = "DevicePort",
  targetNamespace = "http://www.onvif.org/ver10/device/wsdl")
public class JakDeviceImpl implements Device {

	@Override
	public List<Service> GetServices(boolean includeCapability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceServiceCapabilities GetServiceCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetDeviceInformation(Holder<String> manufacturer, Holder<String> model, Holder<String> firmwareVersion,
			Holder<String> serialNumber, Holder<String> hardwareId) {
	  manufacturer.value = new String("john");
	  model.value = new String("onvif-test");
	  firmwareVersion.value = new String("0.0.1");
	  serialNumber.value = new String("001");
	  hardwareId.value = new String("test#1");	
	}

	@Override
	public void SetSystemDateAndTime(SetDateTimeType dateTimeType, boolean daylightSavings, TimeZone timeZone,
			DateTime utcDateTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetSystemFactoryDefault(FactoryDefaultType factoryDefault) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String UpgradeSystemFirmware(AttachmentData firmware) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemDateTime GetSystemDateAndTime() {
	  SystemDateTime res = new SystemDateTime();
	  DateTime dt = new DateTime();
	  
	  res.setUTCDateTime(dt);
	  return res;
	}

	@Override
	public String SystemReboot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RestoreSystem(List<BackupFile> backupFiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BackupFile> GetSystemBackup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemLog GetSystemLog(SystemLogType logType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SupportInformation GetSystemSupportInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Scope> GetScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetScopes(List<String> scopes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddScopes(List<String> scopeItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveScopes(Holder<List<String>> scopeItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DiscoveryMode GetDiscoveryMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetDiscoveryMode(DiscoveryMode discoveryMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DiscoveryMode GetRemoteDiscoveryMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetRemoteDiscoveryMode(DiscoveryMode remoteDiscoveryMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<NetworkHost> GetDPAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetEndpointReferenceResponse GetEndpointReference(
			org.onvif.ver10.device.wsdl.GetEndpointReference parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteUser GetRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetRemoteUser(RemoteUser remoteUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> GetUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void CreateUsers(List<User> user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteUsers(List<String> username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetUser(List<User> user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String GetWsdlUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetPasswordComplexityOptions(Holder<IntRange> minLenRange, Holder<IntRange> uppercaseRange,
			Holder<IntRange> numberRange, Holder<IntRange> specialCharsRange,
			Holder<Boolean> blockUsernameOccurrenceSupported, Holder<Boolean> policyConfigurationLockSupported) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetPasswordComplexityConfiguration(Holder<Integer> minLen, Holder<Integer> uppercase,
			Holder<Integer> number, Holder<Integer> specialChars, Holder<Boolean> blockUsernameOccurrence,
			Holder<Boolean> policyConfigurationLocked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetPasswordComplexityConfiguration(Integer minLen, Integer uppercase, Integer number,
			Integer specialChars, Boolean blockUsernameOccurrence, Boolean policyConfigurationLocked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetPasswordHistoryConfiguration(Holder<Boolean> enabled, Holder<Integer> length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetPasswordHistoryConfiguration(boolean enabled, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetAuthFailureWarningOptions(Holder<IntRange> monitorPeriodRange, Holder<IntRange> authFailureRange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetAuthFailureWarningConfiguration(Holder<Boolean> enabled, Holder<Integer> monitorPeriod,
			Holder<Integer> maxAuthFailures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetAuthFailureWarningConfiguration(boolean enabled, int monitorPeriod, int maxAuthFailures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Capabilities GetCapabilities(List<CapabilityCategory> category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetDPAddresses(List<NetworkHost> dpAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HostnameInformation GetHostname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetHostname(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean SetHostnameFromDHCP(boolean fromDHCP) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DNSInformation GetDNS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetDNS(boolean fromDHCP, List<String> searchDomain, List<IPAddress> dnsManual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NTPInformation GetNTP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetNTP(boolean fromDHCP, List<NetworkHost> ntpManual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DynamicDNSInformation GetDynamicDNS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetDynamicDNS(DynamicDNSType type, String name, Duration ttl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<NetworkInterface> GetNetworkInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SetNetworkInterfaces(String interfaceToken, NetworkInterfaceSetConfiguration networkInterface) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NetworkProtocol> GetNetworkProtocols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetNetworkProtocols(List<NetworkProtocol> networkProtocols) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkGateway GetNetworkDefaultGateway() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetNetworkDefaultGateway(List<String> iPv4Address, List<String> iPv6Address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkZeroConfiguration GetZeroConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetZeroConfiguration(String interfaceToken, boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPAddressFilter GetIPAddressFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveIPAddressFilter(IPAddressFilter ipAddressFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BinaryData GetAccessPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetAccessPolicy(BinaryData policyFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Certificate CreateCertificate(String certificateID, String subject, XMLGregorianCalendar validNotBefore,
			XMLGregorianCalendar validNotAfter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificate> GetCertificates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CertificateStatus> GetCertificatesStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetCertificatesStatus(List<CertificateStatus> certificateStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteCertificates(List<String> certificateID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BinaryData GetPkcs10Request(String certificateID, String subject, BinaryData attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void LoadCertificates(List<Certificate> nvtCertificate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean GetClientCertificateMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void SetClientCertificateMode(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RelayOutput> GetRelayOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetRelayOutputSettings(String relayOutputToken, RelayOutputSettings properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetRelayOutputState(String relayOutputToken, RelayLogicalState logicalState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String SendAuxiliaryCommand(String auxiliaryCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificate> GetCACertificates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void LoadCertificateWithPrivateKey(List<CertificateWithPrivateKey> certificateWithPrivateKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CertificateInformation GetCertificateInformation(String certificateID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void LoadCACertificates(List<Certificate> caCertificate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CreateDot1XConfiguration(Dot1XConfiguration dot1xConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetDot1XConfiguration(Dot1XConfiguration dot1xConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dot1XConfiguration GetDot1XConfiguration(String dot1xConfigurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dot1XConfiguration> GetDot1XConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void DeleteDot1XConfiguration(List<String> dot1xConfigurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetDot11CapabilitiesResponse GetDot11Capabilities(
			org.onvif.ver10.device.wsdl.GetDot11Capabilities parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dot11Status GetDot11Status(String interfaceToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dot11AvailableNetworks> ScanAvailableDot11Networks(String interfaceToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetSystemUris(Holder<SystemLogUriList> systemLogUris, Holder<String> supportInfoUri,
			Holder<String> systemBackupUri, Holder<Extension> extension) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StartFirmwareUpgrade(Holder<String> uploadUri, Holder<Duration> uploadDelay,
			Holder<Duration> expectedDownTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StartSystemRestore(Holder<String> uploadUri, Holder<Duration> expectedDownTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<StorageConfiguration> GetStorageConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String CreateStorageConfiguration(StorageConfigurationData storageConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageConfiguration GetStorageConfiguration(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetStorageConfiguration(StorageConfiguration storageConfiguration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteStorageConfiguration(String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LocationEntity> GetGeoLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetGeoLocation(List<LocationEntity> location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteGeoLocation(List<LocationEntity> location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetHashingAlgorithm(List<String> algorithm) {
		// TODO Auto-generated method stub
		
	}

}
