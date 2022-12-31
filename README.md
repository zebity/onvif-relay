# onvif-relay
An onvif device request relay.
Accepts ONVIF defined SOAP requests and forwards them to ONVIF compliant devices

See: https://tips.graphica.com.au/onvif-ws-client-consumption/

## Tools:

This project uses Apaxche Maven to generate client/server stubs using:
- Java (Open SDK 11)
- Jakarta EE/JAX-WS to parse WSDL and generate Java code
- Apache Maven build system
- Developed and tested on Ubuntu 22.04

Maven Targets:
- mvn -X jaxws:wsimport : to generate java stubs and client
- mvn -X initialize : to download and patch ONVIF wsdl for jaxws:wsimport (runs get-and-patch.sh)

NOTE: Currently automtic download and patch of ONVIF wsdls does not work via Maven, run manually via:
- src/main/sh/get-and-patch.sh src/main/sh/files.txt target/generated-sources/wget/ src/main/resources/META-INF/wsdl/ src/main/patch/- get-and-patch.sh filelist.txt download.dir destination.dir patch.dir


# Author:

John Hartley

# License

None

This repository is just to for purpose of documenting and getting feedback from others working to test/interface with ONVIF devices, 

If you are looking to solve using Jakarta EE/wsimport to automatically generate interfaces to ONVIF device, then please fork and submit pull requests
 
