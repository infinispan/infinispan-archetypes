#!/bin/bash

# Make sure to have credentials for the server jboss-releases-repository configured on your settings.xml

mvn -B release:prepare release:perform
