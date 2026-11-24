#!/bin/sh -v

BINDING_BASE="$1"
WSDL_LIST="$2"

FETCH_UTIL="java ../../src/main/java/WsdlCatalogFetcher.java"

echo "Running Fetch & Catalog for: ${WSDL_LIST}"

${FETCH_UTIL} ${BINDING_BASE} ${WSDL_LIST}

