#!/bin/sh -v

XSLT_DIR="$1"
MVNREPO="$2"/repository
PROJDIR="$3"
WSDL_PREFIX="$4"
_JAVA_VER="java-17"

_JAVA=`update-alternatives --display java | grep -m 1 "${_JAVA_VER}.*priority" | cut  -d \  -f 1,1`

# JAVA=/usr/lib/jvm/java-17-openjdk-amd64/bin/java
JAVA=${_JAVA}
# echo JAVA: "${JAVA}".
# exit 0

DEVICE_WSDL_LOC=/META-INF/wsdl/www.onvif.org/ver10/device/wsdl/onvif_device.wsdl
MEDIA_WSDL_LOC=/META-INF/wsdl/www.onvif.org/ver10/media/wsdl/onvif_media.wsdl

WSDL_DEVICE_DIR=${WSDL_PREFIX}/META-INF/wsdl/www.onvif.org/ver10/device/wsdl
WSDL_MEDIA_DIR=${WSDL_PREFIX}/META-INF/wsdl/www.onvif.org/ver10/media/wsdl

BINDING_DEVICE_DIR=${PROJDIR}/target/generated-sources/xsltproc/device
BINDING_MEDIA_DIR=${PROJDIR}/target/generated-sources/xsltproc/media

DEVICE_WSDL=${WSDL_DEVICE_DIR}/onvif_device.wsdl
DEVICE_BINDING_WSDL=${WSDL_DEVICE_DIR}/devicemgmt.wsdl
DEVICE_BINDING=${BINDING_DEVICE_DIR}/device-binding-01.xml

MEDIA_WSDL=${WSDL_MEDIA_DIR}/onvif_media.wsdl
MEDIA_BINDING_WSDL=${WSDL_MEDIA_DIR}/media.wsdl
MEDIA_BINDING=${BINDING_MEDIA_DIR}/media-binding-01.xml

JAVA_GENERATED_DIR=${PROJDIR}/target/generated-sources/wsimport
CLASSES_GENERATED_DIR=${PROJDIR}/target/classes

mkdir -p ${BINDING_DEVICE_DIR}
mkdir -p ${BINDING_MEDIA_DIR}
mkdir -p ${JAVA_GENERATED_DIR}
mkdir -p ${CLASSES_GENERATED_DIR}

xsltproc -o - -stringparam p1 ${DEVICE_BINDING_WSDL} ${XSLT_DIR}/src/main/xml/extract-operation-methods.xslt ${DEVICE_BINDING_WSDL} > ${DEVICE_BINDING}
xsltproc -o - -stringparam p1 ${MEDIA_BINDING_WSDL} ${XSLT_DIR}/src/main/xml/extract-operation-methods.xslt ${MEDIA_BINDING_WSDL} > ${MEDIA_BINDING}

