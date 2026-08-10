#!/usr/bin/env bash

mvn verify org.codehaus.cargo:cargo-maven3-plugin:run -Dcargo.maven.containerId=tomcat11x    -Dcargo.maven.containerUrl=https://repo.maven.apache.org/maven2/org/apache/tomcat/tomcat/11.0.21/tomcat-11.0.21.zip  -Dcargo.jvmargs="-Dverposter.db=$PWD/db/verposter.db"

