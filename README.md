# onvif-relay
An onvif device request relay.
Accepts JSON'ed ONVIF request and forwards them as SOAP request to ONVIF compilient device.

To
- make request to onvif device: http/s://api/relay/{verXX}/{operation}?uri=http[s]://[user:password@]ip:port/{onvif/device_service}
- get schema info (as JSON) (and avoid needing to read WSDL ;-) ): http[s]://api/schema/{verXX}/{operation}

NOTE: verXX refers to ONVIF WSDL version

This repository also includes onvif device testing framework (skeleton) to allow debugging of ONVIF clients without need for an physical onvif device (camera)


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
- mvn -X package: to install onvif-api into maven package repository

NOTE 1: Currently automatic download and patch of ONVIF wsdls does not work via Maven.

Run manually via:
- $ get-and-patch.sh <filelist.[jax|jak].txt> <download.dir> <destination.dir> <patch.dir>
- src/main/sh/get-and-patch.sh src/main/sh/files.jak.txt target/generated-sources/wget/ src/main/resources/META-INF/wsdl/ src/main/patch/


NOTE 2: You can generate code with javax or jakarta inclusions
- To change between Java EE (import javax.PKG) or Jakarta EE (import jakarta.PKG) requires editing of Maven pom.xml properties (now by selecting the jax or jak tree)
- As WSDL patching is slightly differnt for jax / jak specifing the patch variant is via input into get-patch (as above): files.jax.txt | files.jak.txt
 

NOTE 3: There is no ONVIF WSDL/XSD or generated Java code in this repository. Rather this repository holds the tools to generate this as part of the build process. So if you want to "see" the code then run the generation process.


NOTE 4: The better way to customise wsimport result (ie avoid NOTE 5 using "hacked" wsimport), added xslt script (extract-operation-method.xlst) which will read wsdl and generate  external JAX-WS Binding Customerisation file. This can be included using -b flag with wsimport. Tested with xlstproc, which does not seem to allow use of the "document" method to get the name of input WSDL, so this is passed in explicity via -stringparam:
- xsltproc -o - -stringparam p1 /META-INF/wsdl/www.onvif.org/ver10/device/wsdl/devicemgmt.wsdl src/main/xml/extract-operation-methods.xslt src/main/resources/META-INF/wsdl/www.onvif.org/ver10/device/wsdl/devicemgmt.wsdl > target/generated-sources/xsltproc/device/device-binding-01.xml


NOTE 5: Main branch is now Jakarta (JAK), as metro-jaxws-ri build fails for verson 2.3.5 and so unable to create "hacked" WsImport
- See here for wsimport "hack" : https://github.com/zebity/metro-jax-ws/tree/3.0.2-onvif-jak
- See NOTE 4 as this uses wsimport binding file to provide custom naming (the official way) and it easier (once you know) than hacking wsimport


NOTE 6: see src/main/sh/run-wsimport.sh for shell script that runs the binding customisation generator and then wsimport (for both device & media WSDLs)

NOTE 7: Now refacted to use Maven Modules. This lets you to build and test server/client independently. If yoou want to bulid with jax or cxf instead of metro then change chance onvif-api/pom.xml (this is shared across server and client) and then then rebuild part you want.


## Build Process

After much testing I found the following was reqired to get running service:
1. Wrap each ONVIF wsdl with its own service wrapper wsdl
2. Always have wrapping and wrapped WSDL in the same directory

To Build:


0. Setup symlink from /META-INF -> patched WSDL location: /home/ME/onvif-relay/onvif-api/onvif-cxf-api/src/main/resources/META-INF (note this could vary based on selecting CXF vs Metro build and slso your dev environment location
1. [Optionally: Build & Deploy] wsimport tool that preserves upper case identifiers (wscomple project)
2. Do maven clean
3. Run get and patch script to download & patch required WSDLs
4. [Optionally: Generate XML Binding Customisation scripts]
5. Either:
   - A. Run wsimport using -b customsiation.xml from [4.]
   - or
   - B. Run custom wsimport script to to create Upper case Java Method names


# Author:

John Hartley

# License

BSD-3-Clause license - recognising contributors at this repository

This repository is for purpose of documenting and getting feedback from others working to test/interface with ONVIF devices

If you are looking to solve using Jakarta EE/wsimport to automatically generate interfaces to ONVIF devices, then please fork and submit pull requests

# Status:

- Jan 2023 - Added Embedded Jetty based onvif test device, but not tested
- Jan 2023 - Tested Embedded Jetty based onvif test device, exposing SOAP 1.1 not SOAP 1.2
- Jan 2023 - Resolved Embedded Jetty and SOAP 1.2 issue
- Jan 2023 - See metro-jaxb-ri hack for WsImport that maintains ONVIF upper case
- Feb 2023 - Get -b binding customisation working to avoid hacking wsimport (see blog for usage guide)
- Feb 2023 - Change from metro to cxf v4 (Jakarta), which works and tested against devices and with relay
 
