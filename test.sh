#!/bin/bash
mkdir /tmp/project
cp -a ./ /tmp/project
mvn clean test -f /tmp/project/pom.xml
