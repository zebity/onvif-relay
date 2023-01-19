# onvif-relay
An onvif device request relay.
Accepts ONVIF defined SOAP requests and forwards them to ONVIF compliant devices

See: https://tips.graphica.com.au/onvif-ws-client-consumption/

## Tools:

This project uses Apache Maven to generate client/server stubs using:
- Java (Open JDK 11)
- Java EE JAVAX-WS/JAX-WS to parse WSDL and generate Java code or
- Jakarta EE/JAX-WS to parse WSDL and generate Java code
- Embedded Jetty (version 10 for javax.*)
- Embedded Jetty (version 11 for jakarta.*)
- Apache Maven build system
- Developed and tested on Ubuntu 22.04

Maven Targets:
- mvn -X jaxws:wsimport : to generate java stubs and client
- mvn -X initialize : to download and patch ONVIF wsdl for jaxws:wsimport (runs get-and-patch.sh)
- mvn -X generate-sources : to run the CXF WSDL2Java Generator as alternate to wsimport (for testing)

NOTE 1: Currently automtic download and patch of ONVIF wsdls does not work via Maven.

Run manually via:
- $ get-and-patch.sh <filelist.[jax|jak].txt> <download.dir> <destination.dir> <patch.dir>
- src/main/sh/get-and-patch.sh src/main/sh/files.jak.txt target/generated-sources/wget/ src/main/resources/META-INF/wsdl/ src/main/patch/


NOTE 2: You can generate code with javax or jakarta inclusions
- To change between Java EE (import javax.PKG) or Jakarta EE (import jakarta.PKG) requires editing of Maven pom.xml properties
- As WSDL patching is slightly differnt for jax / jak specify patch variant is via input into get-patch (as above): files.jax.txt | files.jak.txt
 

NOTE 3: There is no ONVIF WSDL/XSD or generated Java code in this repository. Rather this repository holds the tools to generate this as part of the build process. So if you want to "see" the code then run the generation process.


NOTE 4: Main branch is now Jakarta (JAK), as metro-jaxws-ri build fails for verson 2.3.5 and so unable to create "hacked" WsImport
- See here for wsimpport "hack" : https://github.com/zebity/metro-jax-ws/tree/3.0.2-onvif-jak

NOTE 5: As an alternate to using "hacked" wsimport, added xslt script (extract-operation-method.xlst) which will read wsdl and generate  external JAX-WS Binding Customerisation file. This can be included using -b flag with wsimport and should avoid need for hacked wsimport. Tested with xlstproc:
- xsltproc -o - src/main/xml/extract-operation-methods.xslt src/main/resources/META-INF/wsdl/www.onvif.org/ver10/device/wsdl/devicemgmt.wsdl > binding.xml


# Author:

John Hartley

# License

None - at present

This repository is for purpose of documenting and getting feedback from others working to test/interface with ONVIF devices

If you are looking to solve using Jakarta EE/wsimport to automatically generate interfaces to ONVIF devices, then please fork and submit pull requests

# Status:

- Jan 2023 - Added Embedded Jetty based onvif test device, but not tested
- Jan 2023 - Tested Embedded Jetty based onvif test device, exposing SOAP 1.1 not SOAP 1.2
- Jan 2023 - Resolved Embedded Jetty and SOAP 1.2 issue
- Jan 2023 - See metro-jaxb-ri hack for WsImport that maintains ONVIF upper case
 
