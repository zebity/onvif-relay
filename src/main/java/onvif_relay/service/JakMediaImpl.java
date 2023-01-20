/**
 @what Test JAVAX onvif device service
 
 */

package onvif_relay.service;

import java.util.List;

import org.onvif.ver10.Media;
import org.onvif.ver10.media.wsdl.Capabilities;
import org.onvif.ver10.media.wsdl.CreateOSD;
import org.onvif.ver10.media.wsdl.CreateOSDResponse;
import org.onvif.ver10.media.wsdl.DeleteOSD;
import org.onvif.ver10.media.wsdl.DeleteOSDResponse;
import org.onvif.ver10.media.wsdl.GetOSD;
import org.onvif.ver10.media.wsdl.GetOSDOptions;
import org.onvif.ver10.media.wsdl.GetOSDOptionsResponse;
import org.onvif.ver10.media.wsdl.GetOSDResponse;
import org.onvif.ver10.media.wsdl.SetOSD;
import org.onvif.ver10.media.wsdl.SetOSDResponse;
import org.onvif.ver10.media.wsdl.VideoSourceMode;
import org.onvif.ver10.schema.AudioDecoderConfiguration;
import org.onvif.ver10.schema.AudioDecoderConfigurationOptions;
import org.onvif.ver10.schema.AudioEncoderConfiguration;
import org.onvif.ver10.schema.AudioEncoderConfigurationOptions;
import org.onvif.ver10.schema.AudioOutput;
import org.onvif.ver10.schema.AudioOutputConfiguration;
import org.onvif.ver10.schema.AudioOutputConfigurationOptions;
import org.onvif.ver10.schema.AudioSource;
import org.onvif.ver10.schema.AudioSourceConfiguration;
import org.onvif.ver10.schema.AudioSourceConfigurationOptions;
import org.onvif.ver10.schema.MediaUri;
import org.onvif.ver10.schema.MetadataConfiguration;
import org.onvif.ver10.schema.MetadataConfigurationOptions;
import org.onvif.ver10.schema.OSDConfiguration;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.VideoAnalyticsConfiguration;
import org.onvif.ver10.schema.VideoEncoderConfiguration;
import org.onvif.ver10.schema.VideoEncoderConfigurationOptions;
import org.onvif.ver10.schema.VideoSource;
import org.onvif.ver10.schema.VideoSourceConfiguration;
import org.onvif.ver10.schema.VideoSourceConfigurationOptions;

import jakarta.jws.WebService;
import jakarta.xml.ws.Holder;

@WebService(
  name = "Media",
  serviceName = "MediaService",
  portName = "MediaPort",
  targetNamespace = "http://www.onvif.org/ver10")
public class JakMediaImpl implements Media {

	@Override
	public Capabilities GetServiceCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoSource> GetVideoSources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioSource> GetAudioSources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioOutput> GetAudioOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile CreateProfile(String name, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile GetProfile(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Profile> GetProfiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void AddVideoEncoderConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddVideoSourceConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddAudioEncoderConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddAudioSourceConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddPTZConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddVideoAnalyticsConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddMetadataConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddAudioOutputConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddAudioDecoderConfiguration(String profileToken, String configurationToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveVideoEncoderConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveVideoSourceConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveAudioEncoderConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveAudioSourceConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemovePTZConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveVideoAnalyticsConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveMetadataConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveAudioOutputConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveAudioDecoderConfiguration(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteProfile(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<VideoSourceConfiguration> GetVideoSourceConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoEncoderConfiguration> GetVideoEncoderConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioSourceConfiguration> GetAudioSourceConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioEncoderConfiguration> GetAudioEncoderConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoAnalyticsConfiguration> GetVideoAnalyticsConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetadataConfiguration> GetMetadataConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioOutputConfiguration> GetAudioOutputConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioDecoderConfiguration> GetAudioDecoderConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VideoSourceConfiguration GetVideoSourceConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VideoEncoderConfiguration GetVideoEncoderConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioSourceConfiguration GetAudioSourceConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioEncoderConfiguration GetAudioEncoderConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VideoAnalyticsConfiguration GetVideoAnalyticsConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetadataConfiguration GetMetadataConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioOutputConfiguration GetAudioOutputConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioDecoderConfiguration GetAudioDecoderConfiguration(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoEncoderConfiguration> GetCompatibleVideoEncoderConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoSourceConfiguration> GetCompatibleVideoSourceConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioEncoderConfiguration> GetCompatibleAudioEncoderConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioSourceConfiguration> GetCompatibleAudioSourceConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoAnalyticsConfiguration> GetCompatibleVideoAnalyticsConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetadataConfiguration> GetCompatibleMetadataConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioOutputConfiguration> GetCompatibleAudioOutputConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AudioDecoderConfiguration> GetCompatibleAudioDecoderConfigurations(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetVideoSourceConfiguration(VideoSourceConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetVideoEncoderConfiguration(VideoEncoderConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetAudioSourceConfiguration(AudioSourceConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetAudioEncoderConfiguration(AudioEncoderConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetVideoAnalyticsConfiguration(VideoAnalyticsConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetMetadataConfiguration(MetadataConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetAudioOutputConfiguration(AudioOutputConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetAudioDecoderConfiguration(AudioDecoderConfiguration configuration, boolean forcePersistence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public VideoSourceConfigurationOptions GetVideoSourceConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VideoEncoderConfigurationOptions GetVideoEncoderConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioSourceConfigurationOptions GetAudioSourceConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioEncoderConfigurationOptions GetAudioEncoderConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetadataConfigurationOptions GetMetadataConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioOutputConfigurationOptions GetAudioOutputConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioDecoderConfigurationOptions GetAudioDecoderConfigurationOptions(String configurationToken,
			String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetGuaranteedNumberOfVideoEncoderInstances(String configurationToken, Holder<Integer> totalNumber,
			Holder<Integer> jpeg, Holder<Integer> h264, Holder<Integer> mpeg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MediaUri GetStreamUri(StreamSetup streamSetup, String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void StartMulticastStreaming(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StopMulticastStreaming(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetSynchronizationPoint(String profileToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MediaUri GetSnapshotUri(String profileToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VideoSourceMode> GetVideoSourceModes(String videoSourceToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SetVideoSourceMode(String videoSourceToken, String videoSourceModeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<OSDConfiguration> GetOSDs(String configurationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetOSDResponse GetOSD(org.onvif.ver10.media.wsdl.GetOSD parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetOSDOptionsResponse GetOSDOptions(org.onvif.ver10.media.wsdl.GetOSDOptions parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SetOSDResponse SetOSD(org.onvif.ver10.media.wsdl.SetOSD parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateOSDResponse CreateOSD(org.onvif.ver10.media.wsdl.CreateOSD parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteOSDResponse DeleteOSD(org.onvif.ver10.media.wsdl.DeleteOSD parameters) {
		// TODO Auto-generated method stub
		return null;
	}


}
