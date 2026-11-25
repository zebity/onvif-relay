#!/bin/sh -v

XSLT_DIR="$1"
WSDL_PREFIX="$2"
ONVIF_DEVICE_REF="$3"
ONVIF_MEDIA_REF="$4"
ONVIF_PTZ_REF="$5"

BINDING_BASE="$1"
WSDL_LIST="$2"

BINDING_UTIL="../src/main/sh/generate-bindings.sh"

echo "Running Binding Generator"

${BINDING_UTIL} ${XSLT_DIR} ${WSDL_PREFIX} ${ONVIF_DEVICE_REF} ${ONVIF_MEDIA_REF} ${ONVIF_PTZ_REF}

