#!/bin/bash

VERSION=3.0.2-onvif-jak
# VERSION=4.0.0-onvif-jak
WSCOMPILE=wscompile
PROJECT_ROOT=~/Documents/dev/onvif-relay

RETDIR=`pwd`

# 1. git clone hacked metro ri

if [ x"$1" = x"JAXWS" ]; then

cd ${PROJECT_ROOT}

cd .. && ( git clone https://github.com/zebity/metro-jax-ws.git )
cd ${PROJECT_ROOT}/../metro-jax-ws
git checkout ${VERSION}
cd jaxws-ri
mvn package

else

 # 2. create wscomple project
 if [ x"$1" = x"WSCOMPILE" ]; then

   cd ${PROJECT_ROOT}/..
   mvn archetype:generate -DgroupId=com.sun.${WSCOMPILE} -DartifactId=${WSCOMPILE} -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
   cp -R metro-jax-ws/jaxws-ri/tools/wscompile/src/main ${WSCOMPILE}/src
   mkdir -p ${WSCOMPILE}/target/generated-sources && cp -R metro-jax-ws/jaxws-ri/tools/wscompile/target/generated-sources/rsrc-gen ${WSCOMPILE}/target/generated-sources
   cp ${PROJECT_ROOT}/doco/pom.xml.debug-wsimport-${VERSION} ${WSCOMPILE}/pom.xml
   mv ${PROJECT_ROOT}/../${WSCOMPILE}/src/main/java/module-info.java ${PROJECT_ROOT}/../${WSCOMPILE}/src/main/java/module-info.java.bak
 fi

fi

cd ${RETDIR}
