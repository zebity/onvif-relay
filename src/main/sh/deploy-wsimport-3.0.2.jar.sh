#!/bin/sh


groupid=onvif-relay
artifactid=onvif-naming-jaxb-tools
version=3.0.2
packaging=jar

# jarpath=../metro-jax-ws/jaxws-ri/bundles/jaxws-tools/target/jaxws-tools-3.0.2.jar
jarpath=target/jars/jaxb-alternatenamesutil.jar


mvn -X install:install-file -Dfile=${jarpath} -DgroupId=${groupid} -DartifactId=${artifactid} -Dversion=${version} -Dpackaging=${packaging} -DgeneratePom=true
