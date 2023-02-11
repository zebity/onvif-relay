#!/bin/sh -v

MVNREPO="$1"/repository
PROJDIR="$2"
WSCOMPILE="$3"

JAVA=/usr/lib/jvm/java-11-openjdk-amd64/bin/java

WSDL_DEVICE_DIR=/META-INF/wsdl/www.onvif.org/ver10/device/wsdl
WSDL_MEDIA_DIR=/META-INF/wsdl/www.onvif.org/ver10/media/wsdl

BINDING_DEVICE_DIR=target/generated-sources/xsltproc/device
BINDING_MEDIA_DIR=target/generated-sources/xsltproc/media

DEVICE_WSDL=${WSDL_DEVICE_DIR}/onvif_device.wsdl
DEVICE_BINDING_WSDL=${WSDL_DEVICE_DIR}/devicemgmt.wsdl
DEVICE_BINDING=${PROJDIR}/${BINDING_DEVICE_DIR}/device-binding-01.xml

MEDIA_WSDL=${WSDL_MEDIA_DIR}/onvif_media.wsdl
MEDIA_BINDING_WSDL=${WSDL_MEDIA_DIR}/media.wsdl
MEDIA_BINDING=${PROJDIR}/${BINDING_MEDIA_DIR}/media-binding-01.xml

mkdir -p ${PROJDIR}/{BINDING_DEVICE_DIR}
mkdir -p ${PROJDIR}/{BINDING_MEDIA_DIR}

xsltproc -o - -stringparam p1 ${DEVICE_BINDING_WSDL} src/main/xml/extract-operation-methods.xslt ${DEVICE_BINDING_WSDL} > ${DEVICE_BINDING}
xsltproc -o - -stringparam p1 ${MEDIA_BINDING_WSDL} src/main/xml/extract-operation-methods.xslt ${MEDIA_BINDING_WSDL} > ${MEDIA_BINDING}

JAVA_CPATH=${MVNREPO}/org/jvnet/mimepull/mimepull/1.9.15/mimepull-1.9.15.jar:${MVNREPO}/com/fasterxml/woodstox/woodstox-core/6.2.6/woodstox-core-6.2.6.jar:${MVNREPO}/com/sun/activation/jakarta.activation/2.0.1/jakarta.activation-2.0.1.jar:${MVNREPO}/com/sun/mail/jakarta.mail/2.0.1/jakarta.mail-2.0.1.jar:${MVNREPO}/jakarta/xml/ws/jakarta.xml.ws-api/3.0.1/jakarta.xml.ws-api-3.0.1.jar:${MVNREPO}/com/sun/xml/bind/jaxb-xjc/3.0.2/jaxb-xjc-3.0.2.jar:${MVNREPO}/jakarta/annotation/jakarta.annotation-api/2.0.0/jakarta.annotation-api-2.0.0.jar:${MVNREPO}/jakarta/jws/jakarta.jws-api/3.0.0/jakarta.jws-api-3.0.0.jar:${MVNREPO}/org/glassfish/ha/ha-api/3.1.13/ha-api-3.1.13.jar:${MVNREPO}/org/glassfish/gmbal/gmbal-api-only/4.0.3/gmbal-api-only-4.0.3.jar:${MVNREPO}/com/sun/xml/messaging/saaj/saaj-impl/2.0.1/saaj-impl-2.0.1.jar:${MVNREPO}/org/codehaus/woodstox/stax2-api/4.2.1/stax2-api-4.2.1.jar:${MVNREPO}/com/sun/xml/ws/jaxws-tools/3.0.2/jaxws-tools-3.0.2.jar:${MVNREPO}/org/jvnet/staxex/stax-ex/2.0.1/stax-ex-2.0.1.jar:${MVNREPO}/com/sun/xml/bind/jaxb-jxc/3.0.2/jaxb-jxc-3.0.2.jar:${MVNREPO}/org/glassfish/external/management-api/3.2.3/management-api-3.2.3.jar:${MVNREPO}/jakarta/xml/soap/jakarta.xml.soap-api/2.0.1/jakarta.xml.soap-api-2.0.1.jar:${MVNREPO}/com/sun/xml/ws/jaxws-rt/3.0.2/jaxws-rt-3.0.2.jar:${MVNREPO}/com/sun/xml/fastinfoset/FastInfoset/2.0.0/FastInfoset-2.0.0.jar:${MVNREPO}/com/sun/xml/bind/jaxb-impl/3.0.2/jaxb-impl-3.0.2.jar:${MVNREPO}/jakarta/xml/bind/jakarta.xml.bind-api/3.0.1/jakarta.xml.bind-api-3.0.1.jar:${MVNREPO}/com/sun/xml/bind/jaxb-core/3.0.2/jaxb-core-3.0.2.jar:${MVNREPO}/com/sun/xml/stream/buffer/streambuffer/2.0.2/streambuffer-2.0.2.jar

cd ${PROJDIR} && ${JAVA} '-cp' ${JAVA_CPATH} 'com.sun.tools.ws.WsImport' '-keep' '-b' ${DEVICE_BINDING} '-s' ${PROJDIR}/target/generated-sources/wsimport '-d' ${PROJDIR}/target/classes '-extension' '-Xnocompile' '-wsdllocation' ${DEVICE_WSDL} file:${DEVICE_WSDL}

cd ${PROJDIR} && ${JAVA} '-cp' ${JAVA_CPATH} 'com.sun.tools.ws.WsImport' '-keep' '-b' ${MEDIA_BINDING} '-s' ${PROJDIR}/target/generated-sources/wsimport '-d' ${PROJDIR}/target/classes '-extension' '-Xnocompile' '-wsdllocation' ${MEDIA_WSDL} file:${MEDIA_WSDL}


