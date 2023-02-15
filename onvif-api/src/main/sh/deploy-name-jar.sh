#!/bin/sh


groupid=onvif-relay
artifactid=onvif-naming
version=0.0.1
packaging=jar

jarpath=target/jars/jaxb-alternatenamesutil.jar


mvn -X install:install-file -Dfile=${jarpath} -DgroupId=${groupid} -DartifactId=${artifactid} -Dversion=${version} -Dpackaging=${packaging} -DgeneratePom=true
