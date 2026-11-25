#!/bin/sh -v

XSLT_DIR="$1"
WSDL_PREFIX="$2"
ONVIF_DEVICE_REF="$3"
ONVIF_MEDIA_REF="$4"
ONVIF_PTZ_REF="$5"

# _JAVA_VER="java-25"
# _JAVA=`update-alternatives --display java | grep -m 1 "${_JAVA_VER}.*priority" | cut  -d \  -f 1,1`
# JAVA=/usr/lib/jvm/java-17-openjdk-amd64/bin/java
# JAVA=${_JAVA}
# echo JAVA: "${JAVA}".
# exit 0

## ONVIF_DEVICE_REF=http://www.onvif.org/ver10/device/wsdl/devicemgmt.wsdl
## ONVIF_MEDIA_REF=http://www.onvif.org/ver10/media/wsdl/media.wsdl
## ONVIF_DEVICE_REF=devicemgmt.wsdl
## ONVIF_MEDIA_REF=media.wsdl

## DEVICE_WSDL_LOC=/META-INF/wsdl/www.onvif.org/ver10/device/wsdl/onvif_device.wsdl
## MEDIA_WSDL_LOC=/META-INF/wsdl/www.onvif.org/ver10/media/wsdl/onvif_media.wsdl

WSDL_DEVICE_DIR=${WSDL_PREFIX}/META-INF/wsdl/www.onvif.org/ver10/device/wsdl
WSDL_MEDIA_DIR=${WSDL_PREFIX}/META-INF/wsdl/www.onvif.org/ver10/media/wsdl
WSDL_PTZ_DIR=${WSDL_PREFIX}/META-INF/wsdl/www.onvif.org/ver20/ptz/wsdl

DEVICE_BINDING_WSDL=${WSDL_DEVICE_DIR}/devicemgmt.wsdl
DEVICE_BINDING=${WSDL_DEVICE_DIR}/device-binding-01.xml
# DEVICE_WSDL=${WSDL_DEVICE_DIR}/onvif_device.wsdl
# DEVICE_BINDING=${BINDING_DEVICE_DIR}/device-binding-01.xml

MEDIA_BINDING_WSDL=${WSDL_MEDIA_DIR}/media.wsdl
MEDIA_BINDING=${WSDL_MEDIA_DIR}/media-binding-01.xml
# MEDIA_WSDL=${WSDL_MEDIA_DIR}/onvif_media.wsdl

PTZ_BINDING_WSDL=${WSDL_PTZ_DIR}/ptz.wsdl
PTZ_BINDING=${WSDL_PTZ_DIR}/ptz-binding-01.xml
# PTZ_WSDL=${WSDL_MEDIA_DIR}/onvif_ptz.wsdl

xsltproc -o - -stringparam p1 ${ONVIF_DEVICE_REF} ${XSLT_DIR}/src/main/xml/extract-operation-methods.xslt ${DEVICE_BINDING_WSDL} > ${DEVICE_BINDING}
xsltproc -o - -stringparam p1 ${ONVIF_MEDIA_REF} ${XSLT_DIR}/src/main/xml/extract-operation-methods.xslt ${MEDIA_BINDING_WSDL} > ${MEDIA_BINDING}
xsltproc -o - -stringparam p1 ${ONVIF_PTZ_REF} ${XSLT_DIR}/src/main/xml/extract-operation-methods.xslt ${PTZ_BINDING_WSDL} > ${PTZ_BINDING}

